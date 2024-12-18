package com.postvan;

import com.postvan.defaults.HttpClientImpl;
import com.postvan.defaults.JavetScriptRunner;
import com.postvan.defaults.PostVanResponseBodyToJsonNode;
import com.postvan.models.*;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpRequest;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PostVan<HttpRequestArgs, PostVanHttpRes extends PostVanHttpResponse> {
    private PostVanHttpClient<HttpRequestArgs, PostVanHttpRes> httpClient;
    private PostVanRequestTransformer<HttpRequestArgs> requestTransformer;
    private static final PostmanVariablePool postmanVariablePool = new PostmanVariablePool();

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

    private static PostVanHttpClient<HttpRequest, PostVanHttpStringResponse> constructHttpsClient(final Map<String, PostVanCertificateProperties> sslInfo) {
        return new HttpClientImpl(sslInfo);
    }

    public static PostVan<HttpRequest, PostVanHttpJacksonResponse> defaultJacksonInstance() {
        val httpClient = constructHttpClientForJackson();
        val transformer = HttpClientImpl.getTransformer();
        return new PostVan<>(httpClient, transformer);
    }

    public static PostVanHttpClient<HttpRequest, PostVanHttpJacksonResponse> constructHttpClientForJackson() {
        return new HttpClientImpl(new PostVanResponseBodyToJsonNode());
    }

    public static PostVanHttpClient<HttpRequest, PostVanHttpJacksonResponse> constructHttpsClientForJackson(final Map<String, PostVanCertificateProperties> sslInfo) {
        return new HttpClientImpl(new PostVanResponseBodyToJsonNode(), sslInfo);
    }


    public List<PostVanHttpRes> runRequest(final PostmanItem postmanItem) {
        val postmanRequest = postmanItem.getRequest();
        val args = requestTransformer.transform(postmanRequest, postmanVariablePool);
        val eventsMap = Objects.nonNull(postmanItem.getEvents()) && !postmanItem.getEvents().isEmpty() ? postmanItem.getEvents().stream().collect(Collectors.groupingBy(PostmanEvent::getListen)) : Collections.<String, List<PostmanEvent>>emptyMap();
        if (postmanRequest.getExtension() != null) {
            if (postmanRequest.getExtension().getPagination() != null) {
                return runPaginatedRequest(postmanItem);
            }
        }
        final var singleResponse = runSingleRequest(postmanRequest, args, eventsMap);
        return Collections.singletonList(singleResponse);
    }

    private void runPreRequestEvents(final List<PostmanEvent> events) {
        if (Objects.nonNull(events) && !events.isEmpty()) {
            events.stream()
                    .map(PostmanEvent::getScript)
                    .map(PostmanScript::getExec)
                    .forEach(script -> JavetScriptRunner.runPreRequestScript(script, postmanVariablePool));
        }
    }

    private PostVanTestResults runPostRequestEvents(final List<PostmanEvent> events, final PostVanHttpResponse httpResponse) {
        if (Objects.nonNull(events) && !events.isEmpty()) {
            return events.stream()
                    .map(PostmanEvent::getScript)
                    .map(PostmanScript::getExec)
                    .map(script -> JavetScriptRunner.runTestScript(script, postmanVariablePool, httpResponse))
                    .findAny().orElseGet(() -> new PostVanTestResults(0, 0, 0));
        } else {
            return null;
        }
    }

    private PostVanHttpRes runSingleRequest(final PostmanRequest postmanRequest, final HttpRequestArgs args, final Map<String, List<PostmanEvent>> events) {
        runPreRequestEvents(events.get("prerequest"));
        val response = switch (postmanRequest.getMethod()) {
            case GET -> httpClient.get(args);
            case POST -> httpClient.post(args);
            case PUT -> httpClient.put(args);
            case PATCH -> httpClient.patch(args);
            case DELETE -> httpClient.delete(args);
            case HEAD -> httpClient.head(args);
            case OPTIONS -> httpClient.options(args);
            default -> throw new IllegalArgumentException("Cannot find HTTP method!");
        };
        // TODO: report results of test.
        val testResults = runPostRequestEvents(events.get("test"), response);
        return response;
    }

    private PostVanHttpClient<HttpRequestArgs, PostVanHttpRes> constructHttpsClientWithContext(final PostmanRequestSchemaExtension extension) {
        return null;
    }

    private List<PostVanHttpRes> runPaginatedRequest(final PostmanItem postmanItem) {
        var req = postmanItem.getRequest();
        val pagination = req.getExtension().getPagination();
        val transformedRequest = requestTransformer.transform(req, postmanVariablePool);
        val listOfResponses = new ArrayList<PostVanHttpRes>();
        val hasEvents = postmanItem.getEvents() != null && !postmanItem.getEvents().isEmpty();
        val eventsMap = hasEvents ? postmanItem.getEvents().stream().collect(Collectors.groupingBy(PostmanEvent::getListen)) : Collections.<String, List<PostmanEvent>>emptyMap();
        runPreRequestEvents(eventsMap.get("prerequest"));
        var res = runSingleRequest(req, transformedRequest, eventsMap);
        listOfResponses.add(res);
        if (pagination.getNextProperty() != null) {
            while (res.hasNext(pagination.getNextProperty())) {
                runPreRequestEvents(eventsMap.get("prerequest"));
                req = buildNextRequest(req, res.getNext(pagination.getNextProperty()));
                val nextTransformed = requestTransformer.transform(req, postmanVariablePool);
                res = runSingleRequest(req, nextTransformed, eventsMap);
                listOfResponses.add(res);
            }
            val finalResponse = listOfResponses.get(0);
            finalResponse.setResponseBodyFromPaginatedRequest(listOfResponses);
            runPostRequestEvents(eventsMap.get("test"), finalResponse);
            return listOfResponses;
        }

        if (pagination.getTotalCountProperty() != null) {
            var count = pagination.getOffsetSize();
            val totalCount = res.getCount(pagination.getTotalCountProperty());
            while ((listOfResponses.size() * pagination.getOffsetSize()) <= totalCount) {
                req = buildCountRequest(req, Map.entry(pagination.getOffsetProperty(), count), Map.entry(pagination.getPageSizeProperty(), pagination.getPageSize()));
                val nextTransformed = requestTransformer.transform(req, postmanVariablePool);
                res = runSingleRequest(req, nextTransformed, eventsMap);
                count += pagination.getOffsetSize();
                listOfResponses.add(res);
            }
            return listOfResponses;
        }
        throw new IllegalArgumentException("Should not get here!");
    }

    private PostmanRequest buildNextRequest(final PostmanRequest previousReq, final String next) {
        val newReq = previousReq.clone();
        val newUrl = new PostmanUrl();
        newUrl.setRaw(next);
        newReq.setUrl(newUrl);
        return newReq;
    }

    private PostmanRequest buildCountRequest(final PostmanRequest previousReq, final Map.Entry<String, Long> offset, Map.Entry<String, Long> count) {
        val newReq = previousReq.clone();
        val queryParams = Map.of(offset.getKey(), offset.getValue().toString(), count.getKey(), count.getValue().toString());
        newReq.setQueryParametersFromMap(queryParams);
        return newReq;
    }

    public List<PostVanHttpRes> runCollection(final PostmanCollection collection) {
        val responses = new ArrayList<PostVanHttpRes>();
        val info = collection.getInfo();
        if (collection.getEvents() != null) {
            runPreRequestEvents(collection.getEvents().stream().filter(event -> StringUtils.equals(event.getListen(), "prerequest")).toList());
        }
        if (info.getExtensions() != null) {
            if (info.getExtensions().getCertificates() != null && !info.getExtensions().getCertificates().isEmpty()) {
                this.httpClient.insertHttpsConfiguration(info.getExtensions().getCertificates());
            }
        }
        val items = collection.getItem();
        for (val item :
                items) {
            responses.addAll(this.runRequest(item));
        }
        return responses;
    }
}
