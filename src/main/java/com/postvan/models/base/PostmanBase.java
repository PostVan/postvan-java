package com.postvan.models.base;

import lombok.Data;

@Data
public class PostmanBase extends RuntimeSafePOJO {
    private String id;
    private String name;
}
