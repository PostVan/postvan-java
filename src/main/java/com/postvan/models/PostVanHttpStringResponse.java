package com.postvan.models;

import com.caoccao.javet.annotations.V8Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class PostVanHttpStringResponse extends PostVanHttpResponse<String> {
    private int statusCode;
    private static final ObjectMapper mapper = new ObjectMapper();

    public PostVanHttpStringResponse(final int statusCode, final String responseBody) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    @Override
    public boolean hasNext(final String key) {
        return false;
    }

    @Override
    public String getNext(String key) {
        return null;
    }

    @Override
    public Long getCount(String key) {
        throw new IllegalArgumentException("Not implemented.");
    }

    @Override
    public Map<String, Object> json() {
        try {
            val value = mapper.readTree(this.responseBody);
            return mapper.convertValue(value, Map.class);
        } catch (final Exception ex) {
            throw new NotImplementedException(ex);
        }
    }

    @Override
    public void setResponseBodyFromPaginatedRequest(final List<PostVanHttpResponse<String>> postVanHttpResponses) {
        // TODO: don't be naive about these strings being JSON
        this.responseBody = "[" + postVanHttpResponses.stream().map(PostVanHttpResponse::getResponseBody).collect(Collectors.joining()) + "]";
    }


}
