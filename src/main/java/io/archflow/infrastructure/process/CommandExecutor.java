package io.archflow.infrastructure.process;

import java.util.List;

/**
 * Abstraction over ProcessBuilder for executing system commands.
 * Centralizes process creation, output capture, and exit code handling.
 */
public class CommandExecutor {

    /**
     * Executes the given command and returns the result.
     *
     * @param command list of command tokens (e.g. ["zip", "-r", "out.zip", "dir/"])
     * @return result with exit code and stderr output
     */
    public CommandResult execute(List<String> command) {
        // TODO: implement using ProcessBuilder
        throw new UnsupportedOperationException("Not implemented yet");
    }
}