package org.acme.auth;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TenantAuthorizationPolicy {

    @Inject
    SecurityIdentity securityIdentity;

    public boolean isGranted(Integer requiredTenant) {
        if (requiredTenant == null) {
            return true;
        }
        TenantCredential tenantCredential = securityIdentity.getCredential(TenantCredential.class);
        return requiredTenant.equals(tenantCredential.getTenantId());
    }
}