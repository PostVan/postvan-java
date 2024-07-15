import com.fasterxml.jackson.databind.ObjectMapper;
import com.postvan.Postvan;
import com.postvan.models.PostmanCollection;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PostvanTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testConstruct() throws Exception {
        val instance = Postvan.defaultInstance();
        assertNotNull(instance);
    }

    @Test
    void testDeserialize() throws Exception {
        val collectionStr = IOUtils.resourceToString("/HTTPBin.postman_collection.json", StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        assertNotNull(collection);
    }

    @Test
    void testRun() throws Exception {
        val collectionStr = IOUtils.resourceToString("/HTTPBin.postman_collection.json", StandardCharsets.UTF_8);
        val collection = mapper.readValue(collectionStr, PostmanCollection.class);
        try (val instance = Postvan.defaultInstance()) {
            val responses = instance.runCollection(collection);
            assertNotNull(responses);
            for (val response : responses) {
                assertEquals(200, response.getStatusCode());
                System.out.println(response.getResponseBody());
            }
        }
    }

}