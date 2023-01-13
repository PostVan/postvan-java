package com.postvan.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PostmanCollection {
    private PostmanInformation info;
    private List<PostmanItem> item;
    private List<PostmanEvent> event;
    private List<PostmanVariable> variable;
    private PostmanAuth auth;
    private Map<String, Object> protocolProfileBehavior;
}
