package com.postvan.models;

import com.caoccao.javet.annotations.V8Property;

import java.util.HashMap;
import java.util.Map;

public class PostmanJSVariablePool {
    private final Map<String, Map<String, Object>> variablePool = Map.of(
            "collectionVariables", new HashMap<>(),
            "localVariables", new HashMap<>()
    );

    public Object get(final String key) {
        return null;
    }

    public Map<String, Object> getCollectionVariables() {
        return this.variablePool.get("collectionVariables");
    }

    public boolean has(final String key) {
        return false;
    }

    @V8Property
    public void set(final String key, final Object value) {
        final var localVariables = variablePool.get("localVariables");
        localVariables.put(key, value);
    }
}
