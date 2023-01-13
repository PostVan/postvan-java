package com.postvan.models;

public interface PostvanHttpClient<HttpRequestArgs, HttpResponse> {
    HttpResponse get(final HttpRequestArgs arguments);
    HttpResponse post(final HttpRequestArgs  arguments);
    HttpResponse put(final HttpRequestArgs  arguments);
    HttpResponse delete(final HttpRequestArgs  arguments);
    HttpResponse head(final HttpRequestArgs  arguments);
    HttpResponse options(final HttpRequestArgs  arguments);
}
