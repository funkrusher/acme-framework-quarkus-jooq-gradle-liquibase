package org.acme.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.acme.generated.jooq_testshop.tables.interfaces.IProduct;
import org.acme.generated.jooq_testshop.tables.dtos.Product;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * ProductDTO
 */
@Valid
@Schema(name = "Product", description = "Represents a product")
public class ProductDTO extends Product implements IProduct {

    @Schema(writeOnly = true)
    private boolean deleteFlag;

    @Valid
    @Schema(readOnly = true)
    private ProductLangDTO lang;

    @Valid
    private List<ProductLangDTO> langs;

    public ProductDTO() {
        super();
    }

    public ProductDTO(IProduct value) {
        super(value);
    }

    @JsonProperty
    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
        setAt("deleteFlag", deleteFlag);
    }

    @JsonIgnore
    public boolean getDeleteFlag() {
        return deleteFlag;
    }

    @JsonIgnore
    public void setLang(ProductLangDTO lang) {
        this.lang = lang;
        setAt("lang", lang);
    }

    @JsonProperty
    public ProductLangDTO getLang() {
        return lang;
    }

    public List<ProductLangDTO> getLangs() {
        return langs;
    }

    public void setLangs(List<ProductLangDTO> langs) {
        this.langs = langs;
        setAt("langs", langs);
    }
}
