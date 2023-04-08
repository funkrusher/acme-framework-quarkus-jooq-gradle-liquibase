package org.acme.cognito;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.Produces;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.net.URI;

/**
 * Creates CognitoLocal-specific objects
 */
@ApplicationScoped
public class CognitoLocalProducer {
    @Produces
    @Default
    public CognitoIdentityProviderClient createCognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                .region(CognitoLocalSetup.COGNITO_LOCAL_REGION)
                .endpointOverride(URI.create(CognitoLocalSetup.COGNITO_LOCAL_ENDPOINT))
                .credentialsProvider(() -> AwsBasicCredentials.create(CognitoLocalSetup.ACCESSKEY, CognitoLocalSetup.SECRETKEY))
                .build();
    }
}
