package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostmanInformation {
    private String name;
    @JsonProperty("_postman_id")
    private String postmanId;
    private PostmanDescription description;
    private PostmanVersion version;
    private String schema;
}
