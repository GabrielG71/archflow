package io.archflow.infrastructure;

import io.archflow.infrastructure.process.CommandExecutor;
import io.archflow.infrastructure.process.CommandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandExecutorTest {

    private CommandExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new CommandExecutor();
    }

    @Test
    void execute_successfulCommand_returnsExitCodeZero() {
        CommandResult result = executor.execute(List.of("echo", "hello"));
        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void execute_successfulCommand_capturesOutput() {
        CommandResult result = executor.execute(List.of("echo", "archflow"));
        assertThat(result.stdout()).contains("archflow");
    }

    @Test
    void execute_failingCommand_returnsNonZeroExitCode() {
        CommandResult result = executor.execute(List.of("false"));
        assertThat(result.exitCode()).isNotEqualTo(0);
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    void execute_nonExistentBinary_returnsFailureResult() {
        CommandResult result = executor.execute(List.of("__binary_that_does_not_exist__"));
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.stderr()).isNotBlank();
    }

    @Test
    void execute_stderrOutput_mergedIntoStdout() {
        // ls on a non-existent path prints error to stderr
        CommandResult result = executor.execute(List.of("ls", "/path/that/does/not/exist/xyz"));
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.stdout()).isNotBlank();
    }

    @Test
    void executeWithOutputFile_writesStdoutToFile(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("output.txt");

        CommandResult result = executor.executeWithOutputFile(List.of("echo", "hello archflow"), outputFile);

        assertThat(result.isSuccess()).isTrue();
        assertThat(Files.exists(outputFile)).isTrue();
        assertThat(Files.readString(outputFile)).contains("hello archflow");
    }

    @Test
    void executeWithOutputFile_failingCommand_returnsFailureResult(@TempDir Path tempDir) {
        Path outputFile = tempDir.resolve("output.txt");

        CommandResult result = executor.executeWithOutputFile(List.of("false"), outputFile);

        assertThat(result.isSuccess()).isFalse();
    }
}
