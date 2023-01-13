package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanRequest extends RuntimeSafePOJO {
    public HttpMethod method;
    public PostmanUrl url;
}
