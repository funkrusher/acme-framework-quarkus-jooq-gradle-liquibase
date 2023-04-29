package org.acme.util.exception;

import jakarta.validation.ConstraintViolation;

import java.util.HashSet;
import java.util.Set;

/**
 * ValidationException
 * <p>
 * we want to enforce, that the caller catches the ValidationException and handles it accordingly.
 * </p>
 */
public class ValidationException extends Exception {
    private Set<? extends ConstraintViolation<?>> violations;

    public ValidationException(String message) {
        super(message);
    }

    public <T> ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        super("");
        this.violations = violations;
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
}