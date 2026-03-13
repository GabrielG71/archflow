package io.archflow.domain.exception;

/**
 * Thrown when the input file extension does not match any supported format.
 */
public class UnsupportedFormatException extends RuntimeException {

    public UnsupportedFormatException(String extension) {
        super("extensão não compatível: " + extension);
    }
}