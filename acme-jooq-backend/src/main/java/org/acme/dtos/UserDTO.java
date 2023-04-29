package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.jooq_testshop.tables.dtos.Client;
import org.acme.generated.jooq_testshop.tables.dtos.User;
import org.acme.generated.jooq_testshop.tables.interfaces.IClient;
import org.acme.generated.jooq_testshop.tables.interfaces.IUser;

/**
 * UserDTO
 */
@Valid
public class UserDTO extends User implements IUser {
}
