package io.archflow.domain.exception;

/**
 * Thrown when a compression or extraction operation fails at execution time.
 */
public class OperationFailedException extends RuntimeException {

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}