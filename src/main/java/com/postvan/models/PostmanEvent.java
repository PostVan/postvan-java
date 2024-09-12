package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanEvent extends RuntimeSafePOJO {
    private String listen;
    private PostmanScript script;
}
