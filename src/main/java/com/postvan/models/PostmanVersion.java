package com.postvan.models;

import lombok.Data;

import java.util.Map;

@Data
public class PostmanVersion {
    private Integer major;
    private Integer minor;
    private Integer patch;
    private String identifier;
    private Map<String, Object> meta;
}
