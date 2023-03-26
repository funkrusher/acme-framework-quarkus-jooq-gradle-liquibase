package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.testshop.tables.interfaces.IClient;
import org.acme.generated.testshop.tables.dtos.Client;

/**
 * ClientDTO
 */
@Valid
public class ClientDTO extends Client implements IClient {
}
