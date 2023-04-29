package org.acme.rest.exception;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.services.ProductService;
import org.jboss.logging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
@IfBuildProfile("dev")
public class SafeRuntimeExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = Logger.getLogger(SafeRuntimeExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error(exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exception.getMessage() + "\n" + getStackTraceAsString(exception))
                .build();
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}