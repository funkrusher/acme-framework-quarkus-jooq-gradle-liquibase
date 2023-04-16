package org.acme.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.util.cognito.AcmeClaim;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.Principal;
import java.util.function.Supplier;

@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor {

    private static final String ACME_CLAIM = "custom:acme";

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        if (identity.isAnonymous()) {
            return Uni.createFrom().item(identity);
        } else {
            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);

            Principal principal = identity.getPrincipal();
            if (principal instanceof JWTCallerPrincipal) {
                JWTCallerPrincipal jwtPrincipal = (JWTCallerPrincipal) principal;
                String acmeClaimStr = jwtPrincipal.getClaim(ACME_CLAIM);
                if (acmeClaimStr != null) {
                    try {
                        AcmeClaim acmeClaim = objectMapper.readValue(acmeClaimStr, AcmeClaim.class);
                        builder.addCredential(new TenantCredential(acmeClaim.getClientId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return context.runBlocking(builder::build);
        }
    }

}