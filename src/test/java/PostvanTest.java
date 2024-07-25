import com.fasterxml.jackson.databind.ObjectMapper;
import com.postvan.PostVan;
import com.postvan.models.PostmanCollection;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostvanTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testConstruct() throws Exception {
        val instance = PostVan.defaultInstance();
        assertNotNull(instance);
    }

    @ParameterizedTest
    @MethodSource("getListOfCollections")
    void testDeserialize(final String fileName) throws Exception {
        val collectionStr = IOUtils.resourceToString(fileName, StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        assertNotNull(collection);
    }

    @ParameterizedTest
    @MethodSource("getListOfCollections")
    void testRuns(final String fileName) throws Exception {
        val collectionStr = IOUtils.resourceToString(fileName, StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        val instance = PostVan.defaultJacksonInstance();
        val responses = instance.runCollection(collection);
        assertNotNull(responses);
        for (val response : responses) {
            assertEquals(200, response.getStatusCode());
            System.out.println(response.getResponseBody());
        }
    }

    public static Stream<Arguments> getListOfCollections() {
        return Stream.of(
                Arguments.of("/HTTPBin.postman_collection.json"),
                Arguments.of("/PokeAPI.postman_collection.json")
        );
    }
}