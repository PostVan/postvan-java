package com.postvan.models;

import java.util.HashMap;
import java.util.Map;

public class PostmanMap {
    private final Map<String, String> storage = new HashMap<>();

    public void set(final String key, final String value) {
        this.storage.put(key, value);
    }

    public static PostmanMap emptyMap() {
        return new PostmanMap();
    }

    public boolean has(final String key) {
        return this.storage.containsKey(key);
    }

    public void unset(final String key) {
        this.storage.replace(key, "");
    }

    public String get(final String key) {
        return this.storage.get(key);
    }
}
