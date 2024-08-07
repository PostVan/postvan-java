package com.postvan.models;

import java.util.Map;

public interface PostVanHttpClient<HttpRequestArgs, HttpResponse> {
    HttpResponse get(final HttpRequestArgs arguments);

    HttpResponse post(final HttpRequestArgs arguments);

    HttpResponse put(final HttpRequestArgs arguments);

    HttpResponse delete(final HttpRequestArgs arguments);

    HttpResponse head(final HttpRequestArgs arguments);

    HttpResponse options(final HttpRequestArgs arguments);

    HttpResponse patch(final HttpRequestArgs args);

    void insertHttpsConfiguration(Map<String, PostVanCertificateProperties> sslInfo);
}
