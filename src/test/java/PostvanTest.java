import com.fasterxml.jackson.databind.ObjectMapper;
import com.postvan.Postvan;
import com.postvan.models.PostmanCollection;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostvanTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Stream<Arguments> getCollectionPaths() {
        return Stream.of(
                Arguments.of("/collections/HTTPBin.postman_collection.json"),
                Arguments.of("/collections/Ultipro.postman_collection.json")
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
    void testRun(final String collectionPath) throws Exception {
        val collectionStr = IOUtils.resourceToString(collectionPath, StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        try (val instance = Postvan.defaultInstance()) {
            val responses = instance.runCollection(collection);
            assertNotNull(responses);
            for (val response : responses) {
                assertTrue(response.getStatusCode() == 200);
                System.out.println(response.getResponseBody());
            }
        }
    }

}