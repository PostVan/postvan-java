package com.postvan.models;

import java.util.List;

public interface PostmanEnvironmentVariableFetcher<PostmanEnvironmentValues> {
    String replacer(final String valueToReplace);
    String revealer(final String secretToReveal);
    PostmanEnvironmentValues getValues();
}
