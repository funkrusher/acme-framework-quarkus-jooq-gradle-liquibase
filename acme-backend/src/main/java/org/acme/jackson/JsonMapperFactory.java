package org.acme.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JsonMapperFactory {

    public ObjectMapper createDefaultJsonMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules();
        jsonMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jsonMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        jsonMapper.findAndRegisterModules();
        jsonMapper.registerModule(new JavaTimeModule());
        return jsonMapper;
    }
}
