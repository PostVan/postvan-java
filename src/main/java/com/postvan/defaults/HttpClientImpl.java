package com.postvan.defaults;

import com.postvan.models.*;
import com.postvan.models.base.PostmanValue;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log4j2()
public class HttpClientImpl<ResponseType> implements PostVanHttpClient<HttpRequest, PostVanHttpResponse<ResponseType>> {
    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS).build();
    private final Map<String, LazyInitializer<HttpClient>> sslClientPool;

    private final HttpResponse.BodyHandler<ResponseType> httpBodyHandler;

    public HttpClientImpl() {
        this.httpBodyHandler = (HttpResponse.BodyHandler<ResponseType>) new PostVanResponseBodyToStringHandler();
        this.sslClientPool = new HashMap<>();
    }

    public HttpClientImpl(final HttpResponse.BodyHandler<ResponseType> responseTypeBodyHandler) {
        this.httpBodyHandler = responseTypeBodyHandler;
        this.sslClientPool = new HashMap<>();
    }

    public HttpClientImpl(final Map<String, PostVanCertificateProperties> sslClientConfiguration) {
        this.httpBodyHandler = (HttpResponse.BodyHandler<ResponseType>) new PostVanResponseBodyToStringHandler();
        this.sslClientPool = constructPool(sslClientConfiguration);
    }

    public HttpClientImpl(final HttpResponse.BodyHandler<ResponseType> responseTypeBodyHandler, final Map<String, PostVanCertificateProperties> sslClientConfiguration) {
        this.httpBodyHandler = responseTypeBodyHandler;
        this.sslClientPool = constructPool(sslClientConfiguration);
    }

    @Override
    public PostVanHttpResponse<ResponseType> get(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> post(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> put(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> delete(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> head(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> options(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse<ResponseType> patch(final HttpRequest httpRequest) {
        return doRequest(httpRequest);
    }

    private PostVanHttpResponse<ResponseType> doRequest(final HttpRequest args) {
        try {
            val sslTrustStoreURI = "https://" + args.uri().getAuthority();
            if (sslClientPool.containsKey(sslTrustStoreURI)) {
                val sslClient = sslClientPool.get(sslTrustStoreURI).get();
                final HttpResponse<ResponseType> res = sslClient.send(args, httpBodyHandler);
                return PostVanHttpResponseFactory.<ResponseType>fromHttpResponse(res);
            }
            final HttpResponse<ResponseType> res = client.send(args, httpBodyHandler);
            return PostVanHttpResponseFactory.<ResponseType>fromHttpResponse(res);
        } catch (final Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public void insertHttpsConfiguration(final Map<String, PostVanCertificateProperties> sslClientPool) {
        val pool = constructPool(sslClientPool);
        this.sslClientPool.putAll(pool);
    }

    private Map<String, LazyInitializer<HttpClient>> constructPool(final Map<String, PostVanCertificateProperties> sslClientPool) {
        return sslClientPool
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, (value) -> {
                    try {
                        val sslInfo = value.getValue();
                        val keyStore = switch (sslInfo.getCertificateStrategy()) {
                            case KEY_CERT_PAIR -> {
                                val certBytes = Files.readAllBytes(Paths.get(sslInfo.getCertificatePath()));
                                val certFactory = CertificateFactory.getInstance("X.509");
                                val x509Cert = certFactory.generateCertificate(new ByteArrayInputStream(certBytes));
                                val keyStr = Files.readString(Paths.get(sslInfo.getSecretKeyPath()))
                                        .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                                        .replaceAll("\\s+", "");
                                val keyDecodedBytes = Base64.getDecoder().decode(keyStr);
                                val keyObject = new PKCS8EncodedKeySpec(keyDecodedBytes);
                                val keyFactory = KeyFactory.getInstance("RSA");
                                val privateKey = keyFactory.generatePrivate(keyObject);
                                val kStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                kStore.load(null, null);
                                kStore.setKeyEntry("default", privateKey, sslInfo.getSecretKeyPassphrase().toCharArray(), new Certificate[]{x509Cert});
                                yield kStore;
                            }
                            case PKCS12_FILE -> {
                                val kStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                try (var keyStoreInputStream = Files.newInputStream(Paths.get(sslInfo.getKeystorePath()))) {
                                    kStore.load(keyStoreInputStream, sslInfo.getSecretKeyPassphrase().toCharArray());
                                }
                                yield kStore;
                            }
                        };
                        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                        keyManagerFactory.init(keyStore, sslInfo.getSecretKeyPassphrase().toCharArray());
                        val sslContext = SSLContext.getInstance("TLSv1.3");
                        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
                        val sslParams = sslContext.getSupportedSSLParameters();
                        return LazyInitializer.<HttpClient>builder().setInitializer(
                                () -> HttpClient.newBuilder()
                                        .sslContext(sslContext)
                                        .sslParameters(sslParams)
                                        .build()
                        ).get();
                    } catch (final Exception ex) {
                        log.error(ex);
                        return null;
                    }
                }));
    }

    public static PostVanRequestTransformer<HttpRequest> getTransformer() {
        return (postmanRequest) -> {
            var httpRequestBuilder = HttpRequest.newBuilder();
            val method = postmanRequest.getMethod();
            var url = URI.create(postmanRequest.getUrl().getRaw());
            httpRequestBuilder = switch (method) {
                case GET -> httpRequestBuilder.GET();
                case POST -> httpRequestBuilder.POST(HttpRequest.BodyPublishers.noBody());
                case PUT -> httpRequestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                case PATCH -> httpRequestBuilder.method("PATCH", HttpRequest.BodyPublishers.noBody());
                case DELETE -> httpRequestBuilder.DELETE();
                default -> throw new IllegalArgumentException("Cannot find HTTP method!");
            };
            val auth = postmanRequest.getAuth();
            if (auth != null) {
                httpRequestBuilder = switch (auth.getType()) {
                    case BASIC -> {
                        val username = auth.getAuthValues().stream().filter(value -> Objects.equals(value.getKey(), "username")).findFirst().orElse(new PostmanValue());
                        val password = auth.getAuthValues().stream().filter(value -> Objects.equals(value.getKey(), "password")).findFirst().orElse(new PostmanValue());
                        val basicHeader = Base64.getEncoder().encodeToString((username.getValue() + ":" + password.getValue()).getBytes(StandardCharsets.UTF_8));
                        yield httpRequestBuilder.header("Authorization", "Basic " + basicHeader);
                    }
                    case BEARER -> {
                        val bearerToken = auth.getAuthValues().stream().findFirst().map(PostmanValue::getValue).orElse("");
                        yield httpRequestBuilder.header("Authorization", "Bearer " + bearerToken);
                    }
                };
            }
            val headers = postmanRequest.getHeaders();
            if (headers != null) {
                val httpHeaders = headers.stream()
                        .filter(Predicate.not(PostmanHeader::isDisabled))
                        .toList();
                for (val header : httpHeaders) {
                    httpRequestBuilder.setHeader(header.getKey(), header.getValue());
                }
            }
            val queryParameters = postmanRequest.getUrl().getQueryParameters();
            if (!queryParameters.isEmpty()) {
                val queryStr = queryParameters.values().stream()
                        .filter(Predicate.not(PostManQueryParameter::isDisabled))
                        .map(parameter -> "%s=%s".formatted(parameter.getKey(), parameter.getValue()))
                        .collect(Collectors.joining("&"));
                try {
                    url = new URI(url.getScheme(), url.getAuthority(), url.getPath(), queryStr, url.getFragment());
                } catch (final Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return httpRequestBuilder.uri(url).build();
        };
    }
}
