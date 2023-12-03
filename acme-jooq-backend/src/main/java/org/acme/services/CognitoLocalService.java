package org.acme.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.acme.daos.DAOFactory;
import org.acme.daos.record.UserRecordDAO;
import org.acme.daos.record.UserRoleRecordDAO;
import org.acme.dtos.UserDTO;
import org.acme.dtos.UserRoleDTO;
import org.acme.generated.jooq_testshop.tables.records.UserRoleRecord;
import org.acme.jooq.JooqContext;
import org.acme.jooq.JooqContextFactory;
import org.acme.util.cognito.AcmeClaim;
import org.acme.util.request.RequestContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CognitoLocalService
 */
@ApplicationScoped
public class CognitoLocalService {
    private static final Logger LOGGER = Logger.getLogger(CognitoLocalService.class);

    @ConfigProperty(name = "cognitolocal.userpoolid")
    String userPoolId;

    @ConfigProperty(name = "cognitolocal.userpoolclientid")
    String userPoolClientId;

    @Inject
    @Default
    private CognitoIdentityProviderClient cognitoIpc;

    @Inject
    JooqContextFactory jooqContextFactory;

    @Inject
    DAOFactory daoFactory;

    @Inject
    ObjectMapper objectMapper;

    /**
     * Signup a new user into the pool
     *
     * @param clientId clientId
     * @param email    email
     * @param password password
     * @param firstname firstname
     * @param lastname lastname
     * @param roleId roleId
     * @return userSub
     */
    public String signup(
            Integer clientId,
            String email,
            String password,
            String firstname,
            String lastname,
            String roleId) {

        RequestContext requestContext = new RequestContext(clientId, 1);
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        UserRecordDAO userRecordDAO = daoFactory.createUserRecordDAO(jooqContext);
        UserRoleRecordDAO userRoleRecordDAO = daoFactory.createUserRoleRecordDAO(jooqContext);

        UserDTO user = new UserDTO();
        user.setClientId(clientId);
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);

        UserDTO createdUser = null;
        UserRoleDTO createdUserRole = null;
        String userSub = null;
        try {
            createdUser = userRecordDAO.insertAndReturnDTO(user);

            UserRoleDTO userRole = new UserRoleDTO();
            userRole.setUserId(createdUser.getUserId());
            userRole.setRoleId(roleId);
            createdUserRole = userRoleRecordDAO.insertAndReturnDTO(userRole);

            List<String> roles = new ArrayList<>();
            roles.add(roleId);

            AcmeClaim acmeClaim = new AcmeClaim(clientId, createdUser.getUserId(), roles);
            String acmeClaimStr = objectMapper.writeValueAsString(acmeClaim);

            SignUpRequest request = SignUpRequest.builder()
                    .clientId(userPoolClientId)
                    .username(email)
                    .password(password)
                    .userAttributes(
                            AttributeType.builder().name("email").value(email).build(),
                            AttributeType.builder().name("custom:acme").value(acmeClaimStr).build()
                    )
                    .build();
            SignUpResponse response = cognitoIpc.signUp(request);
            userSub = response.userSub();

            AdminConfirmSignUpRequest confirmRequest = AdminConfirmSignUpRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .build();
            AdminConfirmSignUpResponse confirmResponse = cognitoIpc.adminConfirmSignUp(confirmRequest);

        } catch (Exception e) {
            // delete database entries in case of error (manual rollback)
            if (createdUserRole != null) {
                userRoleRecordDAO.delete(createdUserRole.into(new UserRoleRecord()));
            }
            if (createdUser != null) {
                userRecordDAO.deleteById(createdUser.getUserId());
            }
        }
        return userSub;
    }

    /**
     * Signin with an existing user.
     *
     * @param email    email
     * @param password password
     * @return jwtTokens
     * @throws NotAuthorizedException
     */
    public Map<String, String> signin(String email, String password) throws NotAuthorizedException {
        InitiateAuthResponse response = cognitoIpc.initiateAuth(
                InitiateAuthRequest.builder()
                        .clientId(userPoolClientId)
                        .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                        .authParameters(
                                Map.of(
                                        "USERNAME", email,
                                        "PASSWORD", password
                                )
                        )
                        .build()
        );
        AuthenticationResultType result = response.authenticationResult();

        Map<String, String> jwtTokens = new HashMap<>();
        jwtTokens.put("access_token", result.accessToken());
        jwtTokens.put("refresh_token", result.refreshToken());
        jwtTokens.put("id_token", result.idToken());
        return jwtTokens;
    }

    /**
     * Find and return an existing user.
     *
     * @param email email
     * @return user-attributes
     * @throws UserNotFoundException
     */
    public Map<String, String> find(String email) throws UserNotFoundException {
        AdminGetUserRequest request = AdminGetUserRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .build();
        AdminGetUserResponse response = cognitoIpc.adminGetUser(request);

        List<AttributeType> userAttributes = response.userAttributes();
        Map<String, String> attributesMap = new HashMap<>();
        for (AttributeType attribute : userAttributes) {
            attributesMap.put(attribute.name(), attribute.value());
        }
        // The attributes of the user can be accessed through the attributesMap object.
        return attributesMap;
    }


}
