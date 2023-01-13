package com.postvan;

import com.postvan.defaults.ApacheHttpClientImpl;
import com.postvan.defaults.ApacheHttpTransformerImpl;
import com.postvan.models.PostmanCollection;
import com.postvan.models.PostmanRequest;
import com.postvan.models.PostvanHttpClient;
import com.postvan.models.PostvanRequestTransformer;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Postvan<HttpResponse> {
    private PostvanHttpClient httpClient;
    private PostvanRequestTransformer transformer;

    public static Postvan<org.apache.http.HttpResponse> defaultInstance() {
        val httpClient = constructApacheClient();
        val transformer = constructApacheTransformer();
        return new Postvan<>(httpClient, transformer);
    }

    private static PostvanRequestTransformer constructApacheTransformer() {
        return new ApacheHttpTransformerImpl();
    }

    private static PostvanHttpClient constructApacheClient() {
        return new ApacheHttpClientImpl();
    }

    public HttpResponse runRequest(final PostmanRequest postmanRequest) {
        val args = transformer.transform(postmanRequest);
        switch (postmanRequest.getMethod()) {
            case GET:
                return (HttpResponse) httpClient.get(args);
            case POST:
                return (HttpResponse) httpClient.post(args);
            case PUT:
                return (HttpResponse) httpClient.put(args);
            case DELETE:
                return (HttpResponse) httpClient.delete(args);
            case HEAD:
                return (HttpResponse) httpClient.head(args);
            case OPTIONS:
                return (HttpResponse) httpClient.options(args);
            default:
                throw new IllegalArgumentException("Cannot find HTTP method!");
        }
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
}
