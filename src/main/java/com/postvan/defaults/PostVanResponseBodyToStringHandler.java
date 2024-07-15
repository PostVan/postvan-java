package com.postvan.defaults;

import java.nio.charset.StandardCharsets;

import static java.net.http.HttpResponse.*;

public class PostVanResponseBodyToStringHandler implements BodyHandler<String> {
    @Override
    public BodySubscriber<String> apply(final ResponseInfo responseInfo) {
        return BodySubscribers.ofString(StandardCharsets.UTF_8);
    }
}
