package com.postvan.models;

import lombok.Data;

@Data
public class PostmanDescription {
    private String content;
    private String type;
    private PostmanVersion version;
}
