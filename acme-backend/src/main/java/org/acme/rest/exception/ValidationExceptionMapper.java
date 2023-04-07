package org.acme.rest.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.util.exception.ValidationException;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        Map<String, String> violations = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getViolations()) {
            String property = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            violations.put(property, message);
        }

        ErrorMessage errorMessage = new ErrorMessage("Validation failed", violations);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private static class ErrorMessage {
        private String message;
        private Map<String, String> violations;

        public ErrorMessage(String message, Map<String, String> violations) {
            this.message = message;
            this.violations = violations;
        }

        public String getMessage() {
            return message;
        }

        public Map<String, String> getViolations() {
            return violations;
        }
    }
}