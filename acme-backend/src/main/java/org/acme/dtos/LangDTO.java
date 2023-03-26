package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.testshop.tables.interfaces.ILang;
import org.acme.generated.testshop.tables.dtos.Lang;

/**
 * LangDTO
 */
@Valid
public class LangDTO extends Lang implements ILang {
}
