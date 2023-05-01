package org.acme.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProductLangPK implements Serializable {
    public Long productId;
    public Integer langId;
    // ...
    // getters and setters
    // equals() and hashCode() methods
}