package com.postvan.models;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpResponse;

@Setter
@Getter
public abstract class PostVanHttpResponse<ResponseType> {
    protected ResponseType responseBody;
    public abstract boolean hasNext(final String key);

    public abstract String getNext(final String key);
}
