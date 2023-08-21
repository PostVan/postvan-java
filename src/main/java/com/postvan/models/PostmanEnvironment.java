package com.postvan.models;

import com.postvan.defaults.PostmanEnvironmentVariableFetcherImpl;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static PostmanEnvironment instance = null;

    private PostmanEnvironment() {
        this.fetcher = new PostmanEnvironmentVariableFetcherImpl(this);
        this.values = new ArrayList<>();
    }

    public String replacer(final String valueToReplace) {
        return fetcher.replacer(valueToReplace);
    }

    public String revealer(final String secretToReveal) {
        return fetcher.revealer(secretToReveal);
    }

    public static PostmanEnvironment getInstance() {
        if (Objects.isNull(instance)) {
            instance = new PostmanEnvironment();
        }
        return instance;
    }
}


