package io.archflow.infrastructure.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Abstraction over ProcessBuilder for executing system commands.
 * Centralizes process creation, output capture, and exit code handling.
 */
public class CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    /**
     * Executes the given command and returns the result.
     * Stderr is merged into stdout to avoid buffer deadlocks.
     *
     * @param command list of command tokens (e.g. ["zip", "-r", "out.zip", "dir/"])
     * @return result with exit code and combined output
     */
    public CommandResult execute(List<String> command) {
        log.info("Executing command: {}", String.join(" ", command));
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Command succeeded");
            } else {
                log.warn("Command failed. Exit code: {}. Output: {}", exitCode, output.trim());
            }

            return new CommandResult(exitCode, output, "");
        } catch (IOException e) {
            log.error("Failed to start command: {}. Error: {}", String.join(" ", command), e.getMessage());
            return new CommandResult(1, "", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Command execution interrupted: {}", String.join(" ", command));
            return new CommandResult(1, "", "Interrupted");
        }
    }

    /**
     * Executes a command whose stdout is redirected directly to a file.
     * Used for commands that produce binary output (e.g. xz -d -c).
     *
     * @param command    list of command tokens
     * @param outputFile target file to receive the command's stdout
     * @return result with exit code; stderr is captured separately
     */
    public CommandResult executeWithOutputFile(List<String> command, Path outputFile) {
        log.info("Executing command: {} > {}", String.join(" ", command), outputFile);
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectOutput(outputFile.toFile());
            pb.redirectErrorStream(false);
            Process process = pb.start();
            String stderr = new String(process.getErrorStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Command succeeded");
            } else {
                log.warn("Command failed. Exit code: {}. Stderr: {}", exitCode, stderr.trim());
            }

            return new CommandResult(exitCode, "", stderr);
        } catch (IOException e) {
            log.error("Failed to start command: {}. Error: {}", String.join(" ", command), e.getMessage());
            return new CommandResult(1, "", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Command execution interrupted: {}", String.join(" ", command));
            return new CommandResult(1, "", "Interrupted");
        }
    }
}