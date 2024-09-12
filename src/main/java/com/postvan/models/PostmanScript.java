package com.postvan.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.postvan.models.base.PostmanBase;
import lombok.Data;

import java.net.URL;

@Data
public class PostmanScript extends PostmanBase {
    @JsonDeserialize(using = OneOrManyStringDeserializer.class)
    public String exec;
    private String type;
    public PostmanUrl url;
}
