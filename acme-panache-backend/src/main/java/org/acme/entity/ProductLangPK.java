package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class ProductLangPK implements Serializable {
    public Long product;
    public Integer langId;

}