package org.acme.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

/**
 * Jackson Mappers (CSV, JSON, XML) pre-configured for Transfer (Import/Export purpose)
 * Jackson Mappers are thread-safe, but it is expensive to initialize/configure them,
 * so we create them one-time in this class, and can inject them everwhere where they are needed.
 */
@ApplicationScoped
public class TransferJacksonProducer {

    @Produces
    @TransferJsonMapper
    public ObjectMapper createTransferJsonMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules();
        jsonMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jsonMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        jsonMapper.findAndRegisterModules();
        jsonMapper.registerModule(new JavaTimeModule());
        return jsonMapper;
    }

    @Produces
    @TransferCsvMapper
    public CsvMapper createTransferCsvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        csvMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        csvMapper.findAndRegisterModules();
        csvMapper.registerModule(new JavaTimeModule());
        return csvMapper;
    }


}
