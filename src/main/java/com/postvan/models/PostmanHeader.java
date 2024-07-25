package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanHeader extends RuntimeSafePOJO {
    private String key;
    private String value;
    private boolean disabled;
    private String description;
}
