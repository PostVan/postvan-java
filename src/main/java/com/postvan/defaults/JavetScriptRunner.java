package com.postvan.defaults;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.postvan.defaults.support.MyJavetLogger;
import com.postvan.models.*;
import com.yuenyueng.Expectant;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class JavetScriptRunner {
    public static PostVanTestResults runTestScript(final String testScript, final PostmanVariablePool postmanVariablePool, final PostVanHttpResponse response) {
        try (final V8Runtime v8Runtime = V8Host.getV8Instance().createV8Runtime()) {
            v8Runtime.setConverter(new JavetProxyConverter());
            v8Runtime.setLogger(new MyJavetLogger());
            v8Runtime.getGlobalObject().set("pm", new PostManJSRuntime(postmanVariablePool, response));
            final PostManJSRuntime result = v8Runtime.getExecutor(testScript + "pm;").executeObject();
            return result.getTestResults();
        } catch (final JavetException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void runPreRequestScript(final String preRequestScript, final PostmanVariablePool postmanVariablePool) {
        try (final V8Runtime v8Runtime = V8Host.getV8Instance().createV8Runtime()) {
            v8Runtime.setConverter(new JavetProxyConverter());
            v8Runtime.setLogger(new MyJavetLogger());
            v8Runtime.getGlobalObject().set("pm", new PostManJSRuntime(postmanVariablePool, null));
            v8Runtime.getExecutor(preRequestScript).executeVoid();
        } catch (final JavetException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
