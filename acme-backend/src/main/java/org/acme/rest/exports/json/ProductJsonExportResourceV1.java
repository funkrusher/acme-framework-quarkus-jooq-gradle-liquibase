package org.acme.rest.exports.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.acme.dtos.ProductDTO;
import org.acme.dtos.ProductLangDTO;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.acme.generated.testshop.tables.records.ProductRecord;
import org.acme.jackson.CsvMapperFactory;
import org.acme.jackson.JsonMapperFactory;
import org.acme.services.ProductService;
import org.acme.util.request.RequestContext;
import org.jooq.Field;

import java.util.*;


@Path("/api/v1/exports/json/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductJsonExportResourceV1 {

    @Inject
    ProductService productService;

    @Inject
    JsonMapperFactory jsonMapperFactory;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/")
    public Response streamRootJsonFile() {
        RequestContext requestContext = new RequestContext(1, 1);
        var productStream = productService.streamAll(requestContext);

        StreamingOutput streamingOutput = outputStream -> {
            ObjectMapper csvMapper = jsonMapperFactory.createDefaultJsonMapper();

            Iterator<ProductDTO> it = productStream.iterator();

            SequenceWriter sequenceWriter = csvMapper.writerFor(ProductDTO.class).writeValues(outputStream);
            while (it.hasNext()) {
                ProductDTO product = it.next();
                sequenceWriter.write(product);
            }
        };

        return Response
                .ok(streamingOutput, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=product_export.json")
                .build();
    }
}
