package com.postvan.models;

import java.util.List;

public interface PostmanEnvironmentVariableFetcher<PostmanEnvironment> {
    String replacer(final String valueToReplace);
    String revealer(final String secretToReveal);
    PostmanEnvironment getSelf();
}
