package com.postvan.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Objects;

@Data
public class PostVanHttpJacksonResponse extends PostVanHttpResponse<JsonNode> {
    private int statusCode;

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


}
