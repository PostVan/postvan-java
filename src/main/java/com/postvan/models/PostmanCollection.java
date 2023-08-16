package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.postvan.models.base.RuntimeSafePOJO;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PostmanCollection extends RuntimeSafePOJO {
    private PostmanInformation info;
    private List<PostmanItem> item;
    private List<PostmanEvent> event;
    private List<PostmanVariable> variable;
    private PostmanAuth auth;
    private Map<String, Object> protocolProfileBehavior;
}
