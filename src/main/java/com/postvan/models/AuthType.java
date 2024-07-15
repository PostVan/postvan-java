package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AuthType {
    @JsonProperty("basic")
    BASIC,
    @JsonProperty("bearer")
    BEARER
}
