package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "product_lang")
@Cacheable
public class ProductLangEntity extends PanacheEntity {

    public Integer langId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    public ProductEntity product;

    public String name;
    public String description;


    public static PanacheQuery<ProductLangEntity> findInProductIds(List<Long> productIds) {
        return find("productId in ?1", productIds);
    }
}
