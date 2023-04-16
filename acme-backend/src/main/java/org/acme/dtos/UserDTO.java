package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.testshop.tables.dtos.Client;
import org.acme.generated.testshop.tables.dtos.User;
import org.acme.generated.testshop.tables.interfaces.IClient;
import org.acme.generated.testshop.tables.interfaces.IUser;

/**
 * UserDTO
 */
@Valid
public class UserDTO extends User implements IUser {
}
