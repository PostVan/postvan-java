package com.postvan.models;

import com.postvan.defaults.PostmanEnvironmentVariableFetcherImpl;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PostmanEnvironmentTest {


    @Test
    void testDeserialize() {

    }

    @Test
    void testReplacer() {
        val value = new PostmanEnvironmentVariable();
        value.setKey("service_url");
        value.setValue("test.ukg.com");
        val postmanEnvironment = PostmanEnvironment.getInstance();
        postmanEnvironment.setValues(Collections.singletonList(value));
        val newValue = postmanEnvironment.replacer("https://{{service_url}}/UtmOdataServices/api/OrgLevel1");
        assertEquals("https://test.ukg.com/UtmOdataServices/api/OrgLevel1", newValue);
        System.out.println(newValue);
    }

    @Test
    void testReplacerWithNoValues() {
        val value = new PostmanEnvironmentVariable();
        value.setKey("service_url");
        value.setValue("test.ukg.com");
        val postmanEnvironment = PostmanEnvironment.getInstance();
        postmanEnvironment.setValues(Collections.singletonList(value));
        val newValue = postmanEnvironment.replacer("https://{{service_endpoint}}/UtmOdataServices/api/OrgLevel1");
        assertEquals("https://{{service_endpoint}}/UtmOdataServices/api/OrgLevel1", newValue);
        System.out.println(newValue);
    }

}