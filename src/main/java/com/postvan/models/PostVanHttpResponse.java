package com.postvan.models;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.postvan.defaults.JavetScriptRunner;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public abstract class PostVanHttpResponse<ResponseType> {
    protected ResponseType responseBody;

    public abstract boolean hasNext(final String key);

    public abstract String getNext(final String key);

    public abstract Long getCount(final String key);

    public abstract Map<String, Object> json();

    public abstract void setResponseBodyFromPaginatedRequest(final List<PostVanHttpResponse<ResponseType>> responses);
}
