package org.acme.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.dtos.ProductDTO;
import org.acme.dtos.ProductLangDTO;
import org.acme.generated.PojoUnitTestSerializer;
import org.acme.generated.testshop.tables.Product;
import org.acme.generated.testshop.tables.ProductLang;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.acme.generated.testshop.tables.records.ProductRecord;
import org.acme.test.InjectTestDbUtil;
import org.acme.test.TestDbLifecycleManager;
import org.acme.test.TestDbUtil;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductResourceV1Test
 */
@QuarkusTest
@QuarkusTestResource(TestDbLifecycleManager.class)
public class ProductResourceV1Test {

    @InjectTestDbUtil
    private static TestDbUtil testDbUtil;

    private static Long insertedId = 0L;

    @Test
    @Order(1)
    public void testCreate() throws IOException {
        List<ProductLangDTO> xLangs = new ArrayList<>();
        ProductLangDTO xLangDTO = new ProductLangDTO();
        xLangDTO.setLangId(1);
        xLangDTO.setName("Mein Produkt1");
        xLangDTO.setDescription("Meine Description 1");
        xLangs.add(xLangDTO);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setClientId(1);
        productDTO.setPrice(new BigDecimal("10.00"));
        productDTO.setLangs(xLangs);

        String json = PojoUnitTestSerializer.serializePojo(productDTO);

        ProductDTO responseDTO = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);

        // verify rest-result is as expected...
        assertEquals(productDTO.getClientId(), responseDTO.getClientId());

        // verify database-content is as expected...
        DSLContext dslContext = testDbUtil.createDSLContext();
        ProductRecord record = dslContext.select().from(Product.PRODUCT).where(Product.PRODUCT.PRODUCTID.eq(responseDTO.getProductId())).fetchOne().into(new ProductRecord());
        assertEquals(record.getProductId(), responseDTO.getProductId());

        Result<org.jooq.Record> xLangRecords = dslContext.select().from(ProductLang.PRODUCT_LANG).where(ProductLang.PRODUCT_LANG.PRODUCTID.eq(responseDTO.getProductId())).fetch();
        ProductLangRecord productLangRecord1 = xLangRecords.get(0).into(new ProductLangRecord());
        assertEquals(productLangRecord1.getProductId(), responseDTO.getProductId());
        assertEquals(productLangRecord1.getName(), xLangDTO.getName());
        assertEquals(productLangRecord1.getDescription(), xLangDTO.getDescription());

        insertedId = record.getProductId();
    }

    @Test
    @Order(2)
    public void testUpdate() throws IOException {
        List<ProductLangDTO> xLangs = new ArrayList<>();

        ProductLangDTO xLangDTO = new ProductLangDTO();
        xLangDTO.setLangId(1);
        xLangDTO.setName("Mein Produkt1 Change");
        xLangDTO.setDescription("Meine Description 1 Change");
        xLangs.add(xLangDTO);

        ProductLangDTO xLang2DTO = new ProductLangDTO();
        xLang2DTO.setLangId(2);
        xLang2DTO.setInsertFlag(true);
        xLang2DTO.setName("Extra Language Name");
        xLang2DTO.setDescription("Extra Language Description");
        xLangs.add(xLang2DTO);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(insertedId);
        productDTO.setClientId(1);
        productDTO.setPrice(new BigDecimal("22.00"));
        productDTO.setDeleted(false);
        productDTO.setLangs(xLangs);

        String json = PojoUnitTestSerializer.serializePojo(productDTO);

        ProductDTO responseDTO = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/v1/products/" + insertedId)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductDTO.class);

        // verify rest-result is as expected...
        assertEquals(productDTO.getClientId(), responseDTO.getClientId());

        // verify database-content is as expected...
        DSLContext dslContext = testDbUtil.createDSLContext();
        ProductRecord record = dslContext.select().from(Product.PRODUCT).where(Product.PRODUCT.PRODUCTID.eq(responseDTO.getProductId())).fetchOne().into(new ProductRecord());
        assertEquals(record.getProductId(), responseDTO.getProductId());
        assertEquals(record.getPrice(), responseDTO.getPrice());

        Result<org.jooq.Record> xLangRecords = dslContext.select().from(ProductLang.PRODUCT_LANG).where(ProductLang.PRODUCT_LANG.PRODUCTID.eq(responseDTO.getProductId())).fetch();


        ProductLangRecord existingXLang1 = dslContext.select().from(ProductLang.PRODUCT_LANG).where(ProductLang.PRODUCT_LANG.PRODUCTID.eq(responseDTO.getProductId())).and(ProductLang.PRODUCT_LANG.LANGID.eq(1)).fetchOne().into(new ProductLangRecord());
        ProductLangRecord existingXLang2 = dslContext.select().from(ProductLang.PRODUCT_LANG).where(ProductLang.PRODUCT_LANG.PRODUCTID.eq(responseDTO.getProductId())).and(ProductLang.PRODUCT_LANG.LANGID.eq(2)).fetchOne().into(new ProductLangRecord());

        assertEquals(existingXLang1.getProductId(), responseDTO.getProductId());
        assertEquals(existingXLang1.getName(), xLangDTO.getName());
        assertEquals(existingXLang1.getDescription(), xLangDTO.getDescription());
        assertEquals(existingXLang2.getProductId(), responseDTO.getProductId());
        assertEquals(existingXLang2.getName(), xLang2DTO.getName());
        assertEquals(existingXLang2.getDescription(), xLang2DTO.getDescription());
    }


    @Test
    @Order(3)
    public void testRead() {
        given()
                .when().get("/api/v1/products/1")
                .then()
                .statusCode(200)
                .body(startsWith("{\"productId\":1,\"clientId\":1,\"price\":10.20"));

        DSLContext dslContext = testDbUtil.createDSLContext();
        ProductRecord record = dslContext.select().from(Product.PRODUCT).where(Product.PRODUCT.PRODUCTID.eq(1L)).fetchOne().into(new ProductRecord());
        assertEquals(record.getProductId(), 1L);
    }

    @Test
    @Order(4)
    public void testDelete() throws IOException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setClientId(1);

        String json = PojoUnitTestSerializer.serializePojo(productDTO);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when().delete("/api/v1/products")
                .then()
                .statusCode(204);

        DSLContext dslContext = testDbUtil.createDSLContext();
        Optional<org.jooq.Record> rec = dslContext.select().from(Product.PRODUCT).where(Product.PRODUCT.PRODUCTID.eq(1L)).fetchOptional();
        assertTrue(rec.isEmpty());
    }


}