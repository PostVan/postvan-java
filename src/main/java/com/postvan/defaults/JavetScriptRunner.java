package com.postvan.defaults;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.postvan.models.PostManJSRuntime;
import com.postvan.models.PostVanTestResults;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JavetScriptRunner {
    public static PostVanTestResults runTestScript(final String testScript) {
        try (final V8Runtime v8Runtime = V8Host.getV8Instance().createV8Runtime()) {
            v8Runtime.setConverter(new JavetProxyConverter());
            v8Runtime.getGlobalObject().set("pm", new PostManJSRuntime());
            final PostManJSRuntime result = v8Runtime.getExecutor(testScript).executeObject();
        } catch (final JavetException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
        return new PostVanTestResults(0, 0, 0);
    }
}
