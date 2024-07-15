package com.postvan;

import com.postvan.defaults.HttpClientImpl;
import com.postvan.models.*;
import lombok.AllArgsConstructor;
import lombok.val;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Postvan<HttpRequestArgs, HttpResponse> implements AutoCloseable {
    private PostvanHttpClient<HttpRequestArgs, HttpResponse> httpClient;
    private PostvanRequestTransformer<HttpRequestArgs> transformer;

    public static Postvan<HttpRequest, PostVanHttpResponse> defaultInstance() {
        val httpClient = constructHttpClient();
        val transformer = constructHttpImplTransformer();
        return new Postvan<>(httpClient, transformer);
    }

    private static PostvanRequestTransformer<HttpRequest> constructHttpImplTransformer() {
        return HttpClientImpl.getTransformer();
    }

    private static PostvanHttpClient<HttpRequest, PostVanHttpResponse> constructHttpClient() {
        return new HttpClientImpl();
    }

    public HttpResponse runRequest(final PostmanRequest postmanRequest) {
        val args = transformer.transform(postmanRequest);
        return switch (postmanRequest.getMethod()) {
            case GET -> httpClient.get(args);
            case POST -> httpClient.post(args);
            case PUT -> httpClient.put(args);
            case PATCH -> httpClient.patch(args);
            case DELETE -> httpClient.delete(args);
            case HEAD -> httpClient.head(args);
            case OPTIONS -> httpClient.options(args);
            default -> throw new IllegalArgumentException("Cannot find HTTP method!");
        };
    }

    public List<HttpResponse> runCollection(final PostmanCollection collection) {
        val responses = new ArrayList<HttpResponse>();
        val items = collection.getItem();
        for (val item :
                items) {
            val request = item.getRequest();
            responses.add(this.runRequest(request));
        }
        return responses;
    }

    @Override
    public void close() throws Exception {
        // TODO: Implement
    }
}
