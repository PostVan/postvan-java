package com.postvan.defaults;

import com.postvan.models.PostvanHttpClient;
import lombok.Cleanup;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

public class ApacheHttpClientImpl implements PostvanHttpClient<HttpUriRequest, HttpResponse> {

    private final HttpClient httpClient = HttpClientBuilder.create().build();
    @Override
    public HttpResponse get(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public HttpResponse post(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public HttpResponse put(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public HttpResponse delete(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public HttpResponse head(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public HttpResponse options(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    private HttpResponse baseDoRequest(final HttpUriRequest arguments) {
        try {
            return httpClient.execute(arguments);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
