package com.postvan.models;

import java.io.Closeable;
import java.io.IOException;

public interface PostvanHttpClient<HttpRequestArgs, HttpResponse> extends Closeable {
    HttpResponse get(final HttpRequestArgs arguments);

    HttpResponse post(final HttpRequestArgs arguments);

    HttpResponse put(final HttpRequestArgs arguments);

    HttpResponse delete(final HttpRequestArgs arguments);

    HttpResponse head(final HttpRequestArgs arguments);

    HttpResponse options(final HttpRequestArgs arguments);

    HttpResponse patch(final HttpRequestArgs args);
}
