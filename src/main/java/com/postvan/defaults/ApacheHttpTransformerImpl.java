package com.postvan.defaults;

import com.postvan.models.PostmanRequest;
import com.postvan.models.PostvanRequestTransformer;
import lombok.val;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class ApacheHttpTransformerImpl implements PostvanRequestTransformer<HttpUriRequest> {
    @Override
    public HttpUriRequest transform(final PostmanRequest request) {
        val method = request.getMethod();
        val url = request.getUrl().getRaw();
        switch (method) {
            case GET:
                return new HttpGet(url);
            default:
                throw new IllegalArgumentException("Cannot find HTTP method!");
        }
    }
}
