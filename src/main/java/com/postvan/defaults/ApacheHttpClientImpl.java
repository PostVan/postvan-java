package com.postvan.defaults;

import com.postvan.models.PostVanHttpResponse;
import com.postvan.models.PostvanHttpClient;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;

@AllArgsConstructor
public class ApacheHttpClientImpl implements PostvanHttpClient<HttpUriRequest, PostVanHttpResponse>, Closeable {

    private static final ResponseHandler<PostVanHttpResponse> DEFAULT_HANDLER = (res) ->
       new PostVanHttpResponse(
               res,
               EntityUtils.toString(res.getEntity()),
               res.getStatusLine().getStatusCode()
       );

    private final CloseableHttpClient httpClient;

    @Override
    public PostVanHttpResponse get(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse post(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse put(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse delete(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse head(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse options(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    @Override
    public PostVanHttpResponse patch(final HttpUriRequest arguments) {
        return baseDoRequest(arguments);
    }

    private PostVanHttpResponse baseDoRequest(final HttpUriRequest arguments) {
        return this.baseDoRequest(arguments, DEFAULT_HANDLER);
    }

    private PostVanHttpResponse baseDoRequest(final HttpUriRequest arguments, final ResponseHandler<PostVanHttpResponse> handler) {
        try {
            return httpClient.execute(arguments, handler);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}
