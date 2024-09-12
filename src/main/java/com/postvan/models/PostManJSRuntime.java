package com.postvan.models;

import com.caoccao.javet.annotations.V8Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2()
public class PostManJSRuntime {

    public final PostmanJSVariablePool variables = new PostmanJSVariablePool();
    public final Map<String, Object> collectionVariables = variables.getCollectionVariables();
    //Example error: Should accept all mime types | AssertionError: expected '*/*' to equal '*/sd*'
    public void test(final String testName, final Runnable testFunction) {
        try {
            testFunction.run();
        } catch (final Exception ex) {
            log.error("%s failed".formatted(testName), ex);
        }
    }
}
