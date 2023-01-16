package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.postvan.models.base.PostmanValue;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

import java.util.List;

@Data
public class PostmanAuth extends RuntimeSafePOJO {
    private AuthType type;
    @JsonAlias({"basic", "bearer"})
    private List<PostmanValue> authValues;
}
