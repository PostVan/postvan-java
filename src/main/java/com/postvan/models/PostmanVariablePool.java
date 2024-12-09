package com.postvan.models;

import com.caoccao.javet.annotations.V8Property;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PostmanVariablePool {
    private final Map<String, PostmanMap> variablePool = Map.of(
            "globalVariables", new PostmanMap(),
            "collectionVariables", new PostmanMap(),
            "environmentVariables", new PostmanMap(),
            "dataVariables", new PostmanMap(),
            "localVariables", new PostmanMap()
    );

    public String get(final String key) {
        return variablePool
                .values()
                .stream()
                .filter(variableMap -> variableMap.has(key))
                .findFirst()
                .orElseGet(PostmanMap::emptyMap)
                .get(key);
    }

    public String computeIfAbsent(final String key, final Supplier<String> defaultValueSupplier) {
        if (this.has(key)) {
            return this.get(key);
        } else {
            return defaultValueSupplier.get();
        }
    }

    public PostmanMap getCollectionVariables() {
        return this.variablePool.get("collectionVariables");
    }

    public boolean has(final String key) {
        return variablePool.values().stream().anyMatch(variableMap -> variableMap.has(key));
    }

    public void set(final String key, final String value) {
        final var localVariables = variablePool.get("localVariables");
        localVariables.set(key, value);
    }

    public void unset(final String key) {
        variablePool.forEach((_key, val) -> val.unset(key));
    }
}
