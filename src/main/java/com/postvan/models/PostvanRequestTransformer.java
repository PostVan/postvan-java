package com.postvan.models;

public interface PostvanRequestTransformer<OutputRequest> {
    OutputRequest transform(final PostmanRequest request);
}
