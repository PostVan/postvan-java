package com.postvan.defaults;

import com.postvan.models.PostVanHttpResponseFactory;
import com.postvan.models.PostmanHeader;
import com.postvan.models.PostVanHttpClient;
import com.postvan.models.PostVanRequestTransformer;
import com.postvan.models.base.PostmanValue;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Predicate;

@Log4j2()
public class HttpClientImpl<ResponseType> implements PostVanHttpClient<HttpRequest, ResponseType> {
    private static final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS).build();

    private final HttpResponse.BodyHandler<ResponseType> httpBodyHandler;

    public HttpClientImpl() {
        this.httpBodyHandler = (HttpResponse.BodyHandler<ResponseType>) new PostVanResponseBodyToStringHandler();
    }

    public HttpClientImpl(final HttpResponse.BodyHandler<ResponseType> responseTypeBodyHandler) {
        this.httpBodyHandler = responseTypeBodyHandler;
    }

    @Override
    public ResponseType get(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType post(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType put(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType delete(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType head(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType options(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public ResponseType patch(final HttpRequest httpRequest) {
        return doRequest(httpRequest);
    }

    private ResponseType doRequest(final HttpRequest args) {
        try {
            val res = client.send(args, httpBodyHandler);
            return (ResponseType) PostVanHttpResponseFactory.fromHttpResponse(res);
        } catch (final Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public static PostVanRequestTransformer<HttpRequest> getTransformer() {
        return (postmanRequest) -> {
            var httpRequestBuilder = HttpRequest.newBuilder();
            val method = postmanRequest.getMethod();
            val url = URI.create(postmanRequest.getUrl().getRaw());
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
            return httpRequestBuilder.uri(url).build();
        };
    }
}
