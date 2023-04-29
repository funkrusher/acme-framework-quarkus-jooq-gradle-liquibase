package org.acme.rest;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entity.ProductEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.net.URI;

@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResourceV1 {

    @GET
    @Operation(summary = "returns the product with the specified id")
    @APIResponse(responseCode = "200", description = "Getting the product with the specified id successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/{productId}")
    public Uni<ProductEntity> getOne(Long productId) throws NotFoundException {
        Uni<ProductEntity> product = ProductEntity.findById(productId);
        if (product == null) {
            throw new NotFoundException();
        }
        return product;
    }

    @POST
    @Operation(summary = "creates a new product")
    @APIResponse(responseCode = "   201", description = "product creation successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/")
    @WithTransaction
    @WithSession
    public Uni<ProductEntity> create(ProductEntity product) {
        return product.persistAndFlush()
                .map(ignore -> product);
    }

    @PUT
    @Operation(summary = "updates an existing product")
    @APIResponse(responseCode = "200", description = "product update successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/{productId}")
    @WithTransaction
    @WithSession
    public Uni<ProductEntity> update(ProductEntity product) {
        return ProductEntity.<ProductEntity>findById(product.productId)
                .invoke(existingProduct -> {
                    existingProduct.updatedAt = product.updatedAt;
                    existingProduct.price = product.price;
                });
    }

    @DELETE
    @Path("/{productId}")
    @Operation(summary = "deletes an existing product")
    @APIResponse(responseCode = "204", description = "product delete successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @WithTransaction
    @WithSession
    public Uni<Boolean> delete(Long productId) {
        return ProductEntity.deleteById(productId);
    }


}
