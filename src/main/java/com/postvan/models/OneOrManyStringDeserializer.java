package com.postvan.models;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OneOrManyStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
        try {
            if (jsonParser.isExpectedStartArrayToken()) {
                final var arrNode = (ArrayNode) jsonParser.getCodec().readTree(jsonParser);
                if (arrNode.isArray()) {
                    return StreamSupport.stream(arrNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.joining());
                }
            }
        } catch (final Exception ex) {
            return jsonParser.getValueAsString();
        }
        throw new IllegalArgumentException("Should not get here!");
    }
}
