package com.postvan;

import com.postvan.defaults.ApacheHttpClientImpl;
import com.postvan.defaults.ApacheHttpTransformerImpl;
import com.postvan.models.*;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class Postvan<HttpResponse> implements Closeable {
    private PostvanHttpClient httpClient;
    private PostvanRequestTransformer transformer;
    private PostmanEnvironment postmanEnvironment;

    public static Postvan<PostVanHttpResponse> defaultInstance() {
        val httpClient = constructApacheClient();
        val transformer = constructApacheTransformer();
        val postVanEnv = PostmanEnvironment.getInstance();
        return new Postvan<>(httpClient, transformer, postVanEnv);
    }

    private static PostvanRequestTransformer constructApacheTransformer() {
        return new ApacheHttpTransformerImpl();
    }

    private static PostvanHttpClient constructApacheClient() {
        val client = HttpClientBuilder.create()
                .setMaxConnTotal(5)
                .build();
        return new ApacheHttpClientImpl(client);
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
            case PATCH:
                return (HttpResponse) httpClient.patch(args);
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

    public void addEnvironmentVariable(final PostmanEnvironmentVariable environmentVariable) {
        this.postmanEnvironment.getValues().add(environmentVariable);
    }

    public void addEnvironmentVariables(final PostmanEnvironmentVariable ...environmentVariable) {
        this.postmanEnvironment.getValues().addAll(Arrays.asList(environmentVariable));
    }

    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}
