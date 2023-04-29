package org.acme.generated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * Help serializing a pojo, without the hassle of the JsonIgnore-Annotations that would normally make problems
 * with the unit-test and lead to wrong json, that always contains the full pojo, and not only the allowed stuff.
 *
 * We use the bookKeepingMap of the pojos recursively and only use this, as the source of our data that we serialize,
 * as it contains only the set data of the pojo and not the data that has never been set.
 */
public class PojoUnitTestSerializer {

    public static String serializePojo(AbstractDTO pojo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(writer);
        PojoUnitTestSerializer.serializePojo(pojo, gen, objectMapper);
        gen.flush();
        String json = writer.toString();
        return json;
    }


    private static void serializePojo(AbstractDTO value, JsonGenerator gen, ObjectMapper mapper) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        for (Map.Entry<String, Object> entry : value.getModifiedFields().entrySet()) {
            gen.writeFieldName(entry.getKey().toString());
            if (entry.getValue() instanceof AbstractDTO) {
                serializePojo((AbstractDTO) entry.getValue(), gen, mapper);
            } else {
                serializeValue(entry.getValue(), gen, mapper);
            }
        }
        gen.writeEndObject();
    }

    private static void serializeValue(Object value, JsonGenerator gen, ObjectMapper mapper) throws IOException {
        if (value instanceof Map) {
            gen.writeStartObject();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                gen.writeFieldName(entry.getKey().toString());
                if (entry.getValue() instanceof AbstractDTO) {
                    serializePojo((AbstractDTO) entry.getValue(), gen, mapper);
                } else {
                    serializeValue(entry.getValue(), gen, mapper);
                }
            }
            gen.writeEndObject();
        } else if (value instanceof List) {
            gen.writeStartArray();
            for (Object item : (List<?>) value) {
                if (item instanceof AbstractDTO) {
                    serializePojo((AbstractDTO) item, gen, mapper);
                } else {
                    serializeValue(item, gen, mapper);
                }
            }
            gen.writeEndArray();
        } else if (value instanceof Iterable) {
            gen.writeStartArray();
            for (Object item : (Iterable<?>) value) {
                if (item instanceof AbstractDTO) {
                    serializePojo((AbstractDTO) item, gen, mapper);
                } else {
                    serializeValue(item, gen, mapper);
                }
            }
            gen.writeEndArray();
        } else {
            mapper.writeValue(gen, value);
        }
    }
}