package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanEnvironmentVariable extends RuntimeSafePOJO {
    private Boolean enabled;
    private String key;
    private String name;
    private String type;
    private String value;
}
