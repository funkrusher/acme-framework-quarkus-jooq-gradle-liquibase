/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.testshop.tables.interfaces;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public interface IProduct extends Serializable {

    /**
     * Setter for <code>testshop.product.productId</code>.
     */
    public void setProductId(Long value);

    /**
     * Getter for <code>testshop.product.productId</code>.
     */
    public Long getProductId();

    /**
     * Setter for <code>testshop.product.clientId</code>.
     */
    public void setClientId(Integer value);

    /**
     * Getter for <code>testshop.product.clientId</code>.
     */
    @NotNull
    public Integer getClientId();

    /**
     * Setter for <code>testshop.product.price</code>.
     */
    public void setPrice(BigDecimal value);

    /**
     * Getter for <code>testshop.product.price</code>.
     */
    @NotNull
    public BigDecimal getPrice();

    /**
     * Setter for <code>testshop.product.createdAt</code>.
     */
    public void setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>testshop.product.createdAt</code>.
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>testshop.product.updatedAt</code>.
     */
    public void setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>testshop.product.updatedAt</code>.
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>testshop.product.deleted</code>.
     */
    public void setDeleted(Boolean value);

    /**
     * Getter for <code>testshop.product.deleted</code>.
     */
    public Boolean getDeleted();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IProduct
     */
    public void from(IProduct from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IProduct
     */
    public <E extends IProduct> E into(E into);
}
