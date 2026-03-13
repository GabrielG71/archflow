package io.archflow.infrastructure.process;

/**
 * Holds the outcome of a system command execution.
 */
public record CommandResult(
        int exitCode,
        String stdout,
        String stderr
) {

    public boolean isSuccess() {
        return exitCode == 0;
    }
}