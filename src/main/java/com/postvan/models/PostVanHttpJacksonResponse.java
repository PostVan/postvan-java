package com.postvan.models;

import com.caoccao.javet.annotations.V8Property;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.val;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class PostVanHttpJacksonResponse extends PostVanHttpResponse<JsonNode> {
    private int statusCode;
    private static final ObjectMapper mapper = new ObjectMapper();

    public PostVanHttpJacksonResponse(final int statusCode, final JsonNode responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    @Override
    public boolean hasNext(final String key) {
        try {
            return responseBody.has(key) &&
                    !responseBody.get(key).isNull() &&
                    Objects.nonNull(responseBody.get(key)) &&
                    !Objects.equals(responseBody.get(key).asText(), "");
        } catch (final NullPointerException ex) {
            return false;
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getNext(final String key) {
        try {
            return responseBody.get(key).asText();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Long getCount(final String key) {
        try {
            return responseBody.get(key).asLong();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Map<String, Object> json() {
        return mapper.convertValue(responseBody, Map.class);
    }

    @Override
    public void setResponseBodyFromPaginatedRequest(final List<PostVanHttpResponse<JsonNode>> postVanHttpResponses) {
        val arrayNode = mapper.createArrayNode();
        postVanHttpResponses.stream().map(PostVanHttpResponse::getResponseBody).forEach(arrayNode::add);
        this.responseBody = arrayNode;
    }


}
