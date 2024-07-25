package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

import java.util.List;

@Data
public class PostmanRequest extends RuntimeSafePOJO implements Cloneable {
    private HttpMethod method;
    private PostmanUrl url;
    private PostmanAuth auth;
    @JsonAlias("header")
    private List<PostmanHeader> headers;
    @JsonAlias("$_extensions")
    private PostmanSchemaExtension extension;

    @Override
    public PostmanRequest clone() {
        try {
            final PostmanRequest clone = (PostmanRequest) super.clone();
            clone.setMethod(method);
            clone.setUrl(url);
            clone.setAuth(auth);
            clone.setHeaders(headers);
            clone.setExtension(extension);
            return clone;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
