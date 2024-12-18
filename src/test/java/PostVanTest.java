import com.fasterxml.jackson.databind.ObjectMapper;
import com.postvan.PostVan;
import com.postvan.Utils;
import com.postvan.models.PostVanHttpJacksonResponse;
import com.postvan.models.PostmanCollection;

import com.postvan.models.PostmanVariablePool;
import lombok.val;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostVanTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testConstruct() throws Exception {
        val instance = PostVan.defaultInstance();
        assertNotNull(instance);
    }

    @ParameterizedTest
    @MethodSource("getListOfCollections")
    void testDeserialize(final String fileName) throws Exception {
        val collectionStr = getStringFromResourceFile(fileName);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        assertNotNull(collection);
    }

    @ParameterizedTest
    @MethodSource("getListOfCollections")
    void testRuns(final String fileName, final List<Consumer<List<PostVanHttpJacksonResponse>>> assertions) throws Exception {
        val collectionStr = getStringFromResourceFile(fileName);
        val newCollectionStr = collectionStr.replaceAll("<REPLACE_ME_WITH_PROJECT_PATH>", System.getProperty("user.dir"));
        val collection = mapper.readValue(newCollectionStr, PostmanCollection.class);
        val instance = PostVan.defaultJacksonInstance();
        val responses = instance.runCollection(collection);
        assertNotNull(responses);
        assertions.forEach((eater) -> eater.accept(responses)); // waka waka waka, lol
    }

    @ParameterizedTest
    @MethodSource("getRegexArguments")
    public void testReplaceRegex(final String testInput, final PostmanVariablePool postmanVariablePool, final String expectedResult) {
        val match = Utils.replaceVariables(testInput, postmanVariablePool);
        assertNotNull(match);
        assertEquals(match, expectedResult);
    }

    private String getStringFromResourceFile(final String fileName) throws Exception {
        val sourceDir = System.getProperty("user.dir");
        val testResourceDir = "/src/test/resources/";
        return Files.readString(Paths.get(sourceDir + testResourceDir + fileName), StandardCharsets.UTF_8);
    }

    public static Stream<Arguments> getListOfCollections() {
        return Stream.of(Arguments.of("/HTTPBin.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {
                    assertEquals(200, response.getStatusCode());
                    System.out.println(response.getResponseBody());
                }))), Arguments.of("/PokeAPI.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {
                    assertEquals(200, response.getStatusCode());
                    System.out.println(response.getResponseBody());
                }))), Arguments.of("/PokeAPI_CountPagination.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {
                    assertEquals(200, response.getStatusCode());
                    System.out.println(response.getResponseBody());
                }))), Arguments.of("/CertAuth.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {
                    assertEquals(200, response.getStatusCode());
                    System.out.println(response.getResponseBody());
                }))),
                Arguments.of("/CertAuth_PFX.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {
                    assertEquals(200, response.getStatusCode());
                    System.out.println(response.getResponseBody());
                }))),
                Arguments.of("/HTTPBin_JS_Test.postman_collection.json", List.of((Consumer<List<PostVanHttpJacksonResponse>>) (list) -> list.forEach(response -> {

                })))
        );
    }

    public static Stream<Arguments> getRegexArguments() {
        val postmanPool = new PostmanVariablePool();
        postmanPool.set("username", "burt");
        postmanPool.set("password", "reynolds");
        return Stream.of(
                Arguments.of(
                        "{{username}}",
                        postmanPool,
                        "burt"
                ),
                Arguments.of("https://httpbin.org/basic-auth/{{username}}/{{password}}", postmanPool, "https://httpbin.org/basic-auth/burt/reynolds")
        );
    }
}