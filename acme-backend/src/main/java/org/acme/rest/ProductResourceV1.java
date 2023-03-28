package org.acme.rest;

import org.acme.util.exception.ValidationException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.acme.dtos.ProductDTO;
import org.acme.services.ProductService;
import org.acme.util.query.QueryParameters;
import org.acme.util.request.RequestContext;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResourceV1 {

    @Inject
    ProductService productService;

    @GET
    @Operation(summary = "returns a list of all products")
    @APIResponse(responseCode = "200", description = "List of all products successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/")
    public List<ProductDTO> query(@BeanParam QueryParameters queryParameters) {
        // TODO: create requestContext from cookie/session info and data.
        RequestContext requestContext = new RequestContext(1, 1);
        return productService.query(requestContext, queryParameters);
    }

    @GET
    @Operation(summary = "returns the product with the specified id")
    @APIResponse(responseCode = "200", description = "Getting the product with the specified id successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/{productId}")
    public ProductDTO getOne(Long productId) throws NotFoundException {
        // TODO: create requestContext from cookie/session info and data.
        RequestContext requestContext = new RequestContext(1, 1);
        return productService.getOne(requestContext, productId).orElseThrow(NotFoundException::new);
    }


    @POST
    @Operation(summary = "creates a new product")
    @APIResponse(responseCode = "201", description = "product creation successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/")
    public Response create(ProductDTO product) throws ValidationException {
        RequestContext requestContext = new RequestContext(1, 1);
        ProductDTO created = productService.create(requestContext, product);
        return Response.ok(created).status(201).build();
    }

    @PUT
    @Operation(summary = "updates an existing product")
    @APIResponse(responseCode = "200", description = "product update successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/{productId}")
    public ProductDTO update(ProductDTO product) {
        RequestContext requestContext = new RequestContext(1, 1);
        return productService.update(requestContext, product);
    }

    @DELETE
    @Operation(summary = "deletes an existing product")
    @APIResponse(responseCode = "204", description = "product delete successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    public Response delete(ProductDTO product) {
        RequestContext requestContext = new RequestContext(1, 1);
        productService.delete(requestContext, product);
        return Response.status(204).build();
    }
}
