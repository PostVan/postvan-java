package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostManQueryParameter extends RuntimeSafePOJO {
    private String key;
    private String value;
    private boolean disabled;
    private String description;

    public PostManQueryParameter(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
}
