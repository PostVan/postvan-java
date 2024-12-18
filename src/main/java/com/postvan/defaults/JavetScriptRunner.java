package com.postvan.defaults;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.interop.options.NodeRuntimeOptions;
import com.caoccao.javet.node.modules.NodeModuleModule;
import com.caoccao.javet.utils.JavetOSUtils;
import com.caoccao.javet.utils.JavetResourceUtils;
import com.postvan.Utils;
import com.postvan.defaults.support.MyJavetLogger;
import com.postvan.models.*;
import com.yuenyueng.Expectant;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Node;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Log4j2
public class JavetScriptRunner {
    private static final String REPLACER_TOKEN = "/* <REPLACE_ME_WITH_CODE> */";

    public static PostVanTestResults runTestScript(final String testScript, final PostmanVariablePool postmanVariablePool, final PostVanHttpResponse response) {
        NodeRuntimeOptions.V8_FLAGS.setUseStrict(false);
        try (final JavetEnginePool<NodeRuntime> enginePool = new JavetEnginePool<>()) {
            enginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            try (final IJavetEngine<NodeRuntime> nodeRuntime = enginePool.getEngine()) {
                NodeRuntime v8Engine = nodeRuntime.<NodeRuntime>getV8Runtime();
                final JavetStandardConsoleInterceptor javetConsoleInterceptor =
                        new JavetStandardConsoleInterceptor(v8Engine);
                javetConsoleInterceptor.register(v8Engine.getGlobalObject());
                v8Engine.setConverter(new JavetProxyConverter());
                v8Engine.setLogger(new MyJavetLogger());
                v8Engine.getGlobalObject().set(
                        "expector", Expectant.class,
                        "variablePool", postmanVariablePool,
                        "response", response
                );
                final var testScriptStr = Utils.readStringFromResource("/sandbox/sandbox.js");
                final HashMap<String, Integer> result = v8Engine.getExecutor(testScriptStr.replace(REPLACER_TOKEN, testScript)).executeObject();

                return PostVanTestResults.fromHashMap(result);
            } catch (final JavetException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        } catch (final JavetException ex) {
            log.error(ex);
            throw new RuntimeException(ex);
        }
    }

    public static void runPreRequestScript(final String preRequestScript, final PostmanVariablePool postmanVariablePool) {
        NodeRuntimeOptions.V8_FLAGS.setUseStrict(false);
        try (final JavetEnginePool<NodeRuntime> enginePool = new JavetEnginePool<>()) {
            enginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            try (final IJavetEngine<NodeRuntime> nodeRuntime = enginePool.getEngine()) {
                NodeRuntime v8Engine = nodeRuntime.<NodeRuntime>getV8Runtime();
                final JavetStandardConsoleInterceptor javetConsoleInterceptor =
                        new JavetStandardConsoleInterceptor(v8Engine);
                javetConsoleInterceptor.register(v8Engine.getGlobalObject());
                v8Engine.setConverter(new JavetProxyConverter());
                v8Engine.setLogger(new MyJavetLogger());
                v8Engine.getGlobalObject().set(
                        "expector", Expectant.class,
                        "variablePool", postmanVariablePool,
                        "response", null
                );
                final var testScriptStr = Utils.readStringFromResource("/sandbox/sandbox.js");
                v8Engine.getExecutor(testScriptStr.replace(REPLACER_TOKEN, preRequestScript)).executeVoid();
            } catch (final JavetException e) {
                log.error(e);
                throw new RuntimeException(e);
            }
        } catch (final JavetException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
