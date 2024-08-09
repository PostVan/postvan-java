package com.postvan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;


@Data
@AllArgsConstructor
public class PostVanHttpStringResponse extends PostVanHttpResponse<String> {
    private int statusCode;

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


}
