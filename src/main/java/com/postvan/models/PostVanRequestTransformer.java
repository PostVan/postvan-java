package com.postvan.models;

@FunctionalInterface
public interface PostVanRequestTransformer<OutputRequest> {
    OutputRequest transform(final PostmanRequest request, final PostmanVariablePool postmanVariablePool);
}
