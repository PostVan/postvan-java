package com.postvan.models;

import com.yuenyueng.Expectant;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Log4j2()
public class PostManJSRuntime {

    public PostManJSRuntime(final PostmanVariablePool variables, final PostVanHttpResponse response) {
        this.variables = variables;
        this.collectionVariables = variables.getCollectionVariables();
        this.response = response;
    }

    public final PostmanVariablePool variables;
    public final PostmanMap collectionVariables;
    public PostVanHttpResponse response;
    private int totalTests = 0;
    private int failedTests = 0;
    private int passedTests = 0;

    //Example error: Should accept all mime types | AssertionError: expected '*/*' to equal '*/sd*'
    public void test(final String testName, final Runnable testFunction) {
        totalTests += 1;
        try {
            testFunction.run();
            passedTests += 1;
        } catch (final Exception ex) {
            log.error("%s failed".formatted(testName), ex);
            failedTests += 1;
        }
    }

    public Expectant expect(final Object value) {
        return Expectant.expect(value);
    }

    public PostVanTestResults getTestResults() {
        return new PostVanTestResults(totalTests, passedTests, failedTests);
    }
}
