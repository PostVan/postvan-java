package com.postvan.models;

@FunctionalInterface
public interface PostvanRequestTransformer<OutputRequest> {
    OutputRequest transform(final PostmanRequest request);
}
