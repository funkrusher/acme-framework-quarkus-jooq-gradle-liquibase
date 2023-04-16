package org.acme.dtos;


import jakarta.validation.Valid;
import org.acme.generated.testshop.tables.dtos.User;
import org.acme.generated.testshop.tables.dtos.UserRole;
import org.acme.generated.testshop.tables.interfaces.IUser;
import org.acme.generated.testshop.tables.interfaces.IUserRole;

/**
 * UserRoleDTO
 */
@Valid
public class UserRoleDTO extends UserRole implements IUserRole {
}
