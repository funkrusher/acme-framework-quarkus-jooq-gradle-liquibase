package org.acme.rest.exception;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@UnlessBuildProfile("dev")
public class DangerousRuntimeExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(DangerousRuntimeExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error(exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
    }
}