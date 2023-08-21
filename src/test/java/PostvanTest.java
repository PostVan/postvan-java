import com.fasterxml.jackson.databind.ObjectMapper;
import com.postvan.Postvan;
import com.postvan.models.*;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostvanTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Stream<Arguments> getCollectionPaths() {
        val ultiproMap = new HashMap<>();
        ultiproMap.put("service_url", "kew33.ulticlock.com");
        ultiproMap.put("username", "hoteleffectivenesspmhg");
        ultiproMap.put("password", "P#!gT;ZKCESV8%LwAr]K");
        return Stream.of(
//                Arguments.of("/collections/HTTPBin.postman_collection.json", Collections.emptyList()),
                Arguments.of("/collections/Ultipro.postman_collection.json", Collections.emptyList(), ultiproMap)
        );
    }

    @Test
    void testConstruct() throws Exception {
        val instance = Postvan.defaultInstance();
        assertNotNull(instance);
    }

    @ParameterizedTest
    @MethodSource("getCollectionPaths")
    void testDeserialize(final String collectionPath) throws Exception {
        val collectionStr = IOUtils.resourceToString(collectionPath, StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        assertNotNull(collection);
    }

    @ParameterizedTest
    @MethodSource("getCollectionPaths")
    void testRun(final String collectionPath, final Collection<Consumer<PostVanHttpResponse>> assertions, final Map<String, String> environmentVariables) throws Exception {
        val collectionStr = IOUtils.resourceToString(collectionPath, StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        try (val instance = Postvan.defaultInstance()) {
            environmentVariables.entrySet().forEach((entry) -> instance.addEnvironmentVariable(
                    getEnvironmentVariableForTest(entry.getKey(), entry.getValue())
            ));
            val responses = instance.runCollection(collection);
            assertNotNull(responses);
            for (val response : responses) {
                assertTrue(response.getStatusCode() == 200);
                if (Objects.nonNull(assertions)) {
                    assertions.forEach(consumer -> consumer.accept(response));
                }
                System.out.println(response.getResponseBody());
            }
        }
    }

    private PostmanEnvironmentVariable getEnvironmentVariableForTest(final String key, final String value) {
        val postmanVar = new PostmanEnvironmentVariable();
        postmanVar.setKey(key);
        postmanVar.setValue(value);
        return postmanVar;
    }

}