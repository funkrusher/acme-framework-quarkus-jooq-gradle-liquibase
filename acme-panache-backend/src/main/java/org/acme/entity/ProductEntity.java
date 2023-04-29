package org.acme.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name="product")
@Cacheable
public class ProductEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long productId;
    public Integer clientId;
    public BigDecimal price;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Boolean deleted;


}
