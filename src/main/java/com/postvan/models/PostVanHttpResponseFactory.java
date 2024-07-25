package com.postvan.models;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PostVanHttpResponseFactory {
    private static final Map<String, Function<HttpResponse, PostVanHttpResponse>> registeredFactories = new HashMap<>();
    public static <ResponseType> PostVanHttpResponse fromHttpResponse(final HttpResponse<ResponseType> responseType) {
        return switch (responseType.body().getClass().getCanonicalName()) {
            case "java.lang.String" -> new PostVanHttpStringResponse(responseType.statusCode(), (String) responseType.body());
            case "com.fasterxml.jackson.databind.node.ObjectNode" -> new PostVanHttpJacksonResponse(responseType.statusCode(), (JsonNode) responseType.body());
            default -> throw new IllegalArgumentException(String.format("No default factory available for classname: %s", responseType.body().getClass().getCanonicalName()));
        };
    }

    public static void registerFactory(final String clazzName, final Function<HttpResponse, PostVanHttpResponse> factory) {
        registeredFactories.put(clazzName, factory);
    }

    public Map<String, Function<HttpResponse, PostVanHttpResponse>> getRegisteredFactories() {
        return registeredFactories;
    }
}
