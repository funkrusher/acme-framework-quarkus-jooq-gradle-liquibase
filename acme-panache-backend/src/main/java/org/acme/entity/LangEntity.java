package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "lang")
@Cacheable
public class LangEntity extends PanacheEntityBase {
    @Id
    @Column(name = "langId")
    public Integer langId;
    public String code;
    public String description;

}
