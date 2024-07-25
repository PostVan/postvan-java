package com.postvan;

import com.postvan.defaults.HttpClientImpl;
import com.postvan.defaults.PostVanResponseBodyToJsonNode;
import com.postvan.models.*;
import lombok.AllArgsConstructor;
import lombok.val;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PostVan<HttpRequestArgs, PostVanHttpRes extends PostVanHttpResponse> {
    private PostVanHttpClient<HttpRequestArgs, PostVanHttpRes> httpClient;
    private PostVanRequestTransformer<HttpRequestArgs> requestTransformer;

    public static PostVan<HttpRequest, PostVanHttpStringResponse> defaultInstance() {
        val httpClient = constructHttpClient();
        val transformer = constructHttpImplTransformer();
        return new PostVan<>(httpClient, transformer);
    }

    private static PostVanRequestTransformer<HttpRequest> constructHttpImplTransformer() {
        return HttpClientImpl.getTransformer();
    }

    private static PostVanHttpClient<HttpRequest, PostVanHttpStringResponse> constructHttpClient() {
        return new HttpClientImpl();
    }

    public static PostVan<HttpRequest, PostVanHttpJacksonResponse> defaultJacksonInstance() {
        val httpClient = constructHttpClientForJackson();
        val transformer = HttpClientImpl.getTransformer();
        return new PostVan<>(httpClient, transformer);
    }

    private static PostVanHttpClient<HttpRequest, PostVanHttpJacksonResponse> constructHttpClientForJackson() {
        return new HttpClientImpl(new PostVanResponseBodyToJsonNode());
    }

    public List<PostVanHttpRes> runRequest(final PostmanRequest postmanRequest) {
        val args = requestTransformer.transform(postmanRequest);
        if (postmanRequest.getExtension() != null && postmanRequest.getExtension().getPagination() != null) {
            return runPaginatedRequest(postmanRequest);
        }
        final var singleResponse = runSingleRequest(postmanRequest, args);
        return List.of(singleResponse);
    }

    private PostVanHttpRes runSingleRequest(final PostmanRequest postmanRequest, final HttpRequestArgs args) {
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

    private List<PostVanHttpRes> runPaginatedRequest(final PostmanRequest postmanRequest) {
        var req = postmanRequest;
        val pagination = req.getExtension().getPagination();
        val transformedRequest = requestTransformer.transform(req);
        var res = runSingleRequest(req, transformedRequest);
        val listOfResponses = new ArrayList<PostVanHttpRes>();
        listOfResponses.add(res);
        while (res.hasNext(pagination.getNextProperty())) {
            req = buildNextRequest(req, res.getNext(pagination.getNextProperty()));
            val nextTransformed = requestTransformer.transform(req);
            res = runSingleRequest(req, nextTransformed);
            listOfResponses.add(res);
        }
        return listOfResponses;
    }

    private PostmanRequest buildNextRequest(final PostmanRequest previousReq, final String next) {
        val newReq = previousReq.clone();
        val newUrl = new PostmanUrl();
        newUrl.setRaw(next);
        newReq.setUrl(newUrl);
        return newReq;
    }

    public List<PostVanHttpRes> runCollection(final PostmanCollection collection) {
        val responses = new ArrayList<PostVanHttpRes>();
        val items = collection.getItem();
        for (val item :
                items) {
            val request = item.getRequest();
            responses.addAll(this.runRequest(request));
        }
        return responses;
    }
}
