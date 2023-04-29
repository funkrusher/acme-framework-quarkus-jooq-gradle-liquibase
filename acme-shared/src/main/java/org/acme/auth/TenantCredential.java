package org.acme.auth;

import io.quarkus.security.credential.Credential;

import java.util.Objects;

public class TenantCredential implements Credential {

    private final Integer tenantId;

    public TenantCredential(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        TenantCredential other = (TenantCredential) obj;
        return Objects.equals(tenantId, other.tenantId);
    }
}