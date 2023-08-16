package com.postvan.models;

import com.postvan.defaults.PostmanEnvironmentVariableFetcherImpl;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostmanEnvironment extends RuntimeSafePOJO {
    private String id;
    private String name;
    private String syncedFilename;
    private Boolean synced;
    private Float timestamp;
    private List<PostmanEnvironmentVariable> values;
    @ToString.Exclude
    private final PostmanEnvironmentVariableFetcher fetcher;

    public PostmanEnvironment() {
        this.fetcher = new PostmanEnvironmentVariableFetcherImpl(this);
    }

    public String replacer(final String valueToReplace) {
        return fetcher.replacer(valueToReplace);
    }

    public String revealer(final String secretToReveal) {
        return fetcher.revealer(secretToReveal);
    }
}


