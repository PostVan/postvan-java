package com.postvan.defaults;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PostVanResponseBodyToJsonNode implements HttpResponse.BodyHandler<JsonNode> {
    private final ObjectMapper objectMapper;

    public PostVanResponseBodyToJsonNode() {
        this.objectMapper = new ObjectMapper();
    }

    public PostVanResponseBodyToJsonNode(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public HttpResponse.BodySubscriber<JsonNode> apply(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                (returnStr) -> stringToJson(returnStr, objectMapper)
        );
    }

    private static JsonNode stringToJson(final String jsonString, final ObjectMapper objectMapper) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
