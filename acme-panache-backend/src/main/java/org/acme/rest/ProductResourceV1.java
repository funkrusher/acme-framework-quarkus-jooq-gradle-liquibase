package org.acme.rest;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.acme.entity.ProductEntity;
import org.acme.util.panache.PanacheQueryFactory;
import org.acme.util.query.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.*;

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
        return product.persistAndFlush();
    }

    @PUT
    @Operation(summary = "updates an existing product")
    @APIResponse(responseCode = "200", description = "product update successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @Path("/{productId}")
    @WithTransaction
    @WithSession
    public Uni<ProductEntity> update(ProductEntity product) {
        return ProductEntity.<ProductEntity>findById(product.id)
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

    @GET
    @Path("/")
    @Operation(summary = "returns a list of all products")
    @APIResponse(responseCode = "200", description = "List of all products successful")
    @APIResponse(responseCode = "500", description = "Server unavailable")
    @WithTransaction
    @WithSession
    public Uni<List<ProductEntity>> query(@BeanParam QueryParameters queryParameters) {
        try {
            PanacheQueryFactory factory = new PanacheQueryFactory(ProductEntity.class);
            PanacheQuery<ProductEntity> panacheQuery = factory.createFromQueryParameters(queryParameters);
            Uni<List<ProductEntity>> result = panacheQuery.list();
            return result;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

}
