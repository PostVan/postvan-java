package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PostmanCollection {
    private PostmanInformation info;
    private List<PostmanItem> item;
    @JsonAlias({"event"})
    private List<PostmanEvent> events;
    private List<PostmanVariable> variable;
    private PostmanAuth auth;
    private Map<String, Object> protocolProfileBehavior;
}
