package io.archflow.infrastructure;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.archive.XzHandler;
import io.archflow.infrastructure.process.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class XzHandlerTest {

    private XzHandler handler;

    @BeforeEach
    void setUp() {
        assumeTrue(binaryExists("xz"), "xz binary not available");
        handler = new XzHandler(new CommandExecutor());
    }

    @Test
    void supportedFormat_returnsXz() {
        assertThat(handler.supportedFormat()).isEqualTo(ArchiveFormat.XZ);
    }

    @Test
    void compress_alwaysReturnsFailure(@TempDir Path tempDir) {
        Path input = tempDir.resolve("file.txt");
        Path output = tempDir.resolve("file.txt.xz");

        OperationResult result = handler.compress(new CompressionRequest(input, output, ArchiveFormat.XZ));

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("MVP");
    }

    @Test
    void extract_validXzFile_extractsToOutputDir(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Create a test .xz file using the xz binary directly
        Path originalFile = tempDir.resolve("data.txt");
        Files.writeString(originalFile, "xz content for archflow");
        Path xzFile = tempDir.resolve("data.txt.xz");
        new ProcessBuilder("xz", "-k", originalFile.toString())
                .start().waitFor();
        // xz -k creates data.txt.xz alongside data.txt
        assertThat(Files.exists(xzFile)).isTrue();

        Path outputDir = tempDir.resolve("extracted");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(xzFile, outputDir, ArchiveFormat.XZ));

        assertThat(result.success()).isTrue();
        Path extractedFile = outputDir.resolve("data.txt");
        assertThat(Files.exists(extractedFile)).isTrue();
        assertThat(Files.readString(extractedFile)).isEqualTo("xz content for archflow");
    }

    @Test
    void extract_nonExistentFile_returnsFailure(@TempDir Path tempDir) throws IOException {
        Path fakeXz = tempDir.resolve("nonexistent.xz");
        Path outputDir = tempDir.resolve("out");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(fakeXz, outputDir, ArchiveFormat.XZ));

        assertThat(result.success()).isFalse();
        assertThat(result.message()).isNotBlank();
    }

    @Test
    void extract_createsOutputDirIfAbsent(@TempDir Path tempDir) throws IOException, InterruptedException {
        Path originalFile = tempDir.resolve("file.bin");
        Files.writeString(originalFile, "binary data");
        new ProcessBuilder("xz", "-k", originalFile.toString()).start().waitFor();
        Path xzFile = tempDir.resolve("file.bin.xz");

        Path outputDir = tempDir.resolve("new_dir"); // does not exist yet

        OperationResult result = handler.extract(new ExtractionRequest(xzFile, outputDir, ArchiveFormat.XZ));

        assertThat(result.success()).isTrue();
        assertThat(Files.isDirectory(outputDir)).isTrue();
    }

    private boolean binaryExists(String binary) {
        try {
            return new ProcessBuilder("which", binary).start().waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
