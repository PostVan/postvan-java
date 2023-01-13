package com.postvan.models.base;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeSafePOJO {
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Object getAdditionalProperties(final String key) {
        return additionalProperties.get(key);
    }

    @JsonAnySetter
    public void setAdditionalProperties(final String key, final Object value) {
        this.additionalProperties.put(key, value);
    }

}
