package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanUrl extends RuntimeSafePOJO {
    private String raw;
}
