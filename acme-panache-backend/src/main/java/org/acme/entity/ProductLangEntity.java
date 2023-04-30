package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "product_lang")
@Cacheable
public class ProductLangEntity extends PanacheEntityBase {
    @Id
    @Column(name = "productId")
    public Long productId;
    @Id
    public Integer langId;
    public String name;
    public String description;


    public static PanacheQuery<ProductLangEntity> findInProductIds(List<Long> productIds) {
        return find("productId in ?1", productIds);
    }
}
