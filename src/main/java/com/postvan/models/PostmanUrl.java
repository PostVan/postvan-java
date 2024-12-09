package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PostmanUrl extends RuntimeSafePOJO {
    private String raw;
    private Map<String, PostManQueryParameter> queryParameters = new HashMap<>();

    public void addQueryParameter(final PostManQueryParameter parameter) {
        this.queryParameters.put(parameter.getKey(), parameter);
    }

    public void replaceVariables(final PostmanVariablePool postmanVariablePool) {
//        if (POSTMAN_VARIABLE_REGEX)
    }
}
