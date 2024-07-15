package com.postvan.defaults;

import com.postvan.models.PostVanHttpResponse;
import com.postvan.models.PostmanAuth;
import com.postvan.models.PostvanHttpClient;
import com.postvan.models.PostvanRequestTransformer;
import com.postvan.models.base.PostmanValue;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.logging.log4j.util.Base64Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Log4j2()
public class HttpClientImpl implements PostvanHttpClient<HttpRequest, PostVanHttpResponse> {
    private static final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS).build();
    @Override
    public PostVanHttpResponse get(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse post(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse put(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse delete(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse head(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse options(final HttpRequest arguments) {
        return doRequest(arguments);
    }

    @Override
    public PostVanHttpResponse patch(final HttpRequest httpRequest) {
        return doRequest(httpRequest);
    }

    private PostVanHttpResponse doRequest(final HttpRequest args) {
        try {
            val res = client.send(args, new PostVanResponseBodyToStringHandler());
            return new PostVanHttpResponse(
                    res,
                    res.body(),
                    res.statusCode()
            );
        } catch (final Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public static PostvanRequestTransformer<HttpRequest> getTransformer() {
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
            return httpRequestBuilder.uri(url).build();
        };
    }
}
