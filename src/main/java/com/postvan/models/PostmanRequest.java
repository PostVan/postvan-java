package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanRequest extends RuntimeSafePOJO {
    private HttpMethod method;
    private PostmanUrl url;
    private PostmanAuth auth;
}
