package com.postvan.models;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.postvan.defaults.JavetScriptRunner;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PostVanHttpResponse<ResponseType> {
    protected ResponseType responseBody;
    protected String testScript;

    public PostVanTestResults getTestResults() {
        return JavetScriptRunner.runTestScript(testScript);
    }

    public abstract boolean hasNext(final String key);

    public abstract String getNext(final String key);

    public abstract Long getCount(final String key);
}
