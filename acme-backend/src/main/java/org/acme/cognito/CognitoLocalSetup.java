package org.acme.cognito;

import org.acme.services.CognitoLocalService;
import org.jboss.logging.Logger;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.net.URI;
import java.util.Arrays;

public class CognitoLocalSetup {

    public static final Logger LOGGER = Logger.getLogger(CognitoLocalService.class);

    public static final String COGNITO_LOCAL_ENDPOINT = "http://localhost:9229";

    public static final Region COGNITO_LOCAL_REGION = Region.EU_CENTRAL_1;

    public static final String ACCESSKEY = "accesskey";

    public static final String SECRETKEY = "secretkey";

    public static final String POOLNAME = "acmepool";

    public static final String CLIENTNAME = "acmepool-client";

    public static void main(String[] args) {
        // Create a CognitoIdentityProviderClient using the AWS SDK for Java
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(COGNITO_LOCAL_REGION)
                .endpointOverride(URI.create(COGNITO_LOCAL_ENDPOINT))
                .credentialsProvider(() -> AwsBasicCredentials.create(ACCESSKEY, SECRETKEY))
                .build();

        // Create a new user pool
        CreateUserPoolRequest poolRequest = CreateUserPoolRequest.builder()
                .poolName(POOLNAME)
                .schema(
                        SchemaAttributeType.builder()
                                .name("custom:acme")
                                .attributeDataType(AttributeDataType.STRING)
                                .developerOnlyAttribute(false)
                                .mutable(true)
                                .required(false)
                                .stringAttributeConstraints(StringAttributeConstraintsType.builder()
                                        .maxLength("2048")
                                        .build())
                                .build())
                .build();
        CreateUserPoolResponse poolResponse = cognitoClient.createUserPool(poolRequest);
        String userPoolId = poolResponse.userPool().id();

        // Create a user pool client
        CreateUserPoolClientRequest clientRequest = CreateUserPoolClientRequest.builder()
                .userPoolId(userPoolId)
                .clientName(CLIENTNAME)
                .generateSecret(true)
                .readAttributes(Arrays.asList("custom:acme")) //Add this line to set the read attributes
                .writeAttributes(Arrays.asList("custom:acme")) //Add this line to set the write attributes
                .build();
        CreateUserPoolClientResponse clientResponse = cognitoClient.createUserPoolClient(clientRequest);
        String userPoolClientId = clientResponse.userPoolClient().clientId();
        String userPoolClientSecret = clientResponse.userPoolClient().clientSecret();

        // print all relevant information to the console, so the user can copy them.
        System.out.println("cognitolocal.userpoolid=" + userPoolId);
        System.out.println("cognitolocal.userpoolclientid=" + userPoolClientId);
        System.out.println("cognitolocal.userpoolclientsecret=" + userPoolClientSecret);
    }
}
