package org.acme.jackson;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class CsvMapperFactory {

    public CsvMapper createDefaultCsvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        csvMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        csvMapper.findAndRegisterModules();
        csvMapper.registerModule(new JavaTimeModule());
        return csvMapper;
    }
}
