package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanInformation extends RuntimeSafePOJO {
    private String name;
    @JsonProperty("_postman_id")
    private String postmanId;
    private PostmanDescription description;
    private PostmanVersion version;
    private String schema;
    @JsonAlias("$_extensions")
    private PostmanInfoSchemaExtension extensions;
}
