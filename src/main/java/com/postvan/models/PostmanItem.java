package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.postvan.models.base.PostmanBase;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PostmanItem extends PostmanBase {
    private PostmanDescription description;
    private List<PostmanVariable> variable;
    private List<PostmanEvent> event;
    private PostmanRequest request;
    private List<PostmanResponse> response;
    private Map<String, Object> protocolProfileBehavior;
}
