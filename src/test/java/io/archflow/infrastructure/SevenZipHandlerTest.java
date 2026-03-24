package io.archflow.infrastructure;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.archive.SevenZipHandler;
import io.archflow.infrastructure.process.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class SevenZipHandlerTest {

    private SevenZipHandler handler;

    @BeforeEach
    void setUp() {
        assumeTrue(binaryExists("7z") || binaryExists("7zz"), "7z/7zz binary not available");
        handler = new SevenZipHandler(new CommandExecutor());
    }

    @Test
    void supportedFormat_returnsSevenZip() {
        assertThat(handler.supportedFormat()).isEqualTo(ArchiveFormat.SEVEN_ZIP);
    }

    @Test
    void compress_singleFile_creates7z(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("hello.txt");
        Files.writeString(inputFile, "hello archflow");
        Path outputFile = tempDir.resolve("output.7z");

        OperationResult result = handler.compress(new CompressionRequest(inputFile, outputFile, ArchiveFormat.SEVEN_ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputFile)).isTrue();
    }

    @Test
    void compress_directory_creates7z(@TempDir Path tempDir) throws IOException {
        Path inputDir = tempDir.resolve("mydir");
        Files.createDirectory(inputDir);
        Files.writeString(inputDir.resolve("a.txt"), "content a");
        Path outputFile = tempDir.resolve("output.7z");

        OperationResult result = handler.compress(new CompressionRequest(inputDir, outputFile, ArchiveFormat.SEVEN_ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputFile)).isTrue();
    }

    @Test
    void extract_valid7z_extractsFiles(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("data.txt");
        Files.writeString(inputFile, "7z content");
        Path archiveFile = tempDir.resolve("archive.7z");
        handler.compress(new CompressionRequest(inputFile, archiveFile, ArchiveFormat.SEVEN_ZIP));

        Path outputDir = tempDir.resolve("extracted");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(archiveFile, outputDir, ArchiveFormat.SEVEN_ZIP));

        assertThat(result.success()).isTrue();
        assertThat(Files.list(outputDir).count()).isGreaterThan(0);
    }

    @Test
    void extract_nonExistentFile_returnsFailure(@TempDir Path tempDir) throws IOException {
        Path fakeArchive = tempDir.resolve("nonexistent.7z");
        Path outputDir = tempDir.resolve("out");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(fakeArchive, outputDir, ArchiveFormat.SEVEN_ZIP));

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
