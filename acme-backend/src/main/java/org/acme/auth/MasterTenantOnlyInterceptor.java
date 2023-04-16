package org.acme.auth;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@MasterTenantOnly
public class MasterTenantOnlyInterceptor {

    @Inject
    SecurityIdentity securityIdentity;

    @AroundInvoke
    public Object checkMasterTenantAccess(InvocationContext ctx) throws Exception {
        TenantCredential tenantCredential = securityIdentity.getCredential(TenantCredential.class);
        if (tenantCredential == null || !tenantCredential.getTenantId().equals(1)) {
            throw new ForbiddenException("Access denied for master tenant only.");
        }
        return ctx.proceed();
    }
}