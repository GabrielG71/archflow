package io.archflow.infrastructure;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.archive.ZipHandler;
import io.archflow.infrastructure.process.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ZipHandlerTest {

    private ZipHandler handler;

    @BeforeEach
    void setUp() {
        assumeTrue(binaryExists("zip"), "zip binary not available");
        assumeTrue(binaryExists("unzip"), "unzip binary not available");
        handler = new ZipHandler(new CommandExecutor());
    }

    @Test
    void supportedFormat_returnsZip() {
        assertThat(handler.supportedFormat()).isEqualTo(ArchiveFormat.ZIP);
    }

    @Test
    void compress_singleFile_createsZip(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("hello.txt");
        Files.writeString(inputFile, "hello archflow");
        Path outputZip = tempDir.resolve("output.zip");

        OperationResult result = handler.compress(new CompressionRequest(inputFile, outputZip, ArchiveFormat.ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputZip)).isTrue();
    }

    @Test
    void compress_directory_createsZip(@TempDir Path tempDir) throws IOException {
        Path inputDir = tempDir.resolve("mydir");
        Files.createDirectory(inputDir);
        Files.writeString(inputDir.resolve("a.txt"), "content a");
        Files.writeString(inputDir.resolve("b.txt"), "content b");
        Path outputZip = tempDir.resolve("output.zip");

        OperationResult result = handler.compress(new CompressionRequest(inputDir, outputZip, ArchiveFormat.ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputZip)).isTrue();
    }

    @Test
    void extract_validZip_extractsFiles(@TempDir Path tempDir) throws IOException {
        // Create a zip first
        Path inputFile = tempDir.resolve("data.txt");
        Files.writeString(inputFile, "zip content");
        Path zipFile = tempDir.resolve("archive.zip");
        handler.compress(new CompressionRequest(inputFile, zipFile, ArchiveFormat.ZIP));

        Path outputDir = tempDir.resolve("extracted");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(zipFile, outputDir, ArchiveFormat.ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.list(outputDir).count()).isGreaterThan(0);
    }

    @Test
    void extract_nonExistentFile_returnsFailure(@TempDir Path tempDir) {
        Path fakeZip = tempDir.resolve("nonexistent.zip");
        Path outputDir = tempDir.resolve("out");

        OperationResult result = handler.extract(new ExtractionRequest(fakeZip, outputDir, ArchiveFormat.ZIP));

        assertThat(result.success()).isFalse();
        assertThat(result.message()).isNotBlank();
    }

    private boolean binaryExists(String binary) {
        try {
            return new ProcessBuilder("which", binary).start().waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
