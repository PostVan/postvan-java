package com.postvan.models;

import java.util.HashMap;

public record PostVanTestResults(int totalTests, int passedTests, int failedTests) {
    public static PostVanTestResults fromHashMap(final HashMap<String, Integer> result) {
        return new PostVanTestResults(result.get("totalTests"), result.get("passedTests"), result.get("failedTests"));
    }
}
