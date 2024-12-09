package com.postvan.models;

import com.postvan.models.base.PostmanBase;
import lombok.Data;

@Data
public class PostmanVariable extends PostmanBase {
    private String key;
    private Object value;
    private String description;
    private boolean disabled;
}
