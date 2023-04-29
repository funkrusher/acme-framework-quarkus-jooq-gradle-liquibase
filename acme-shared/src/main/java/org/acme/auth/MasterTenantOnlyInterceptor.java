package org.acme.auth;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import static org.acme.auth.AcmeSecurityIdentity.MASTER_TENANT_ID;

@Interceptor
@MasterTenantOnly
public class MasterTenantOnlyInterceptor {

    @Inject
    SecurityIdentity securityIdentity;

    @AroundInvoke
    public Object checkMasterTenantAccess(InvocationContext ctx) throws Exception {
        TenantCredential tenantCredential = securityIdentity.getCredential(TenantCredential.class);
        if (tenantCredential == null || !tenantCredential.getTenantId().equals(MASTER_TENANT_ID)) {
            throw new ForbiddenException("Access for tenant denied!");
        }
        return ctx.proceed();
    }
}