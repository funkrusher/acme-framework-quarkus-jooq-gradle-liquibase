package org.acme.auth;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AcmeSecurityIdentity {

    public static final Integer MASTER_TENANT_ID = 1;

    @Inject
    SecurityIdentity securityIdentity;

    public Integer getClientId() {
        TenantCredential tenantCredential = securityIdentity.getCredential(TenantCredential.class);
        return tenantCredential.getTenantId();
    }

    public boolean hasClientAccess(Integer requiredClientId) {
        if (requiredClientId == null) {
            return true;
        }
        Integer tenantId = getClientId();
        if (tenantId == null) {
            return false;
        } else if (tenantId.equals(MASTER_TENANT_ID)) {
            // master tenant has access to all other tenants.
            return true;
        }
        return requiredClientId.equals(tenantId);
    }

    public boolean hasRoleAccess(String role) {
        return securityIdentity.hasRole(role);
    }

    public boolean hasOneOfRoles(String... roles) {
        for (String role : roles) {
            if (securityIdentity.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}