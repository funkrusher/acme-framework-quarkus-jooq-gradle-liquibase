package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.jooq_testshop.tables.interfaces.ILang;
import org.acme.generated.jooq_testshop.tables.dtos.Lang;

/**
 * LangDTO
 */
@Valid
public class LangDTO extends Lang implements ILang {
}
