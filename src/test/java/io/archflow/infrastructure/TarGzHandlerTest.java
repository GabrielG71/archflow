package io.archflow.infrastructure;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.archive.TarGzHandler;
import io.archflow.infrastructure.process.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class TarGzHandlerTest {

    private TarGzHandler handler;

    @BeforeEach
    void setUp() {
        assumeTrue(binaryExists("tar"), "tar binary not available");
        handler = new TarGzHandler(new CommandExecutor());
    }

    @Test
    void supportedFormat_returnsTarGz() {
        assertThat(handler.supportedFormat()).isEqualTo(ArchiveFormat.TAR_GZ);
    }

    @Test
    void compress_singleFile_createsTarGz(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("hello.txt");
        Files.writeString(inputFile, "hello archflow");
        Path outputTar = tempDir.resolve("output.tar.gz");

        OperationResult result = handler.compress(new CompressionRequest(inputFile, outputTar, ArchiveFormat.TAR_GZ));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputTar)).isTrue();
    }

    @Test
    void compress_directory_createsTarGz(@TempDir Path tempDir) throws IOException {
        Path inputDir = tempDir.resolve("mydir");
        Files.createDirectory(inputDir);
        Files.writeString(inputDir.resolve("a.txt"), "content a");
        Files.writeString(inputDir.resolve("b.txt"), "content b");
        Path outputTar = tempDir.resolve("output.tar.gz");

        OperationResult result = handler.compress(new CompressionRequest(inputDir, outputTar, ArchiveFormat.TAR_GZ));

        assertThat(result.success()).isTrue();
        assertThat(Files.exists(outputTar)).isTrue();
    }

    @Test
    void extract_validTarGz_extractsFiles(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("data.txt");
        Files.writeString(inputFile, "tar content");
        Path tarFile = tempDir.resolve("archive.tar.gz");
        handler.compress(new CompressionRequest(inputFile, tarFile, ArchiveFormat.TAR_GZ));

        Path outputDir = tempDir.resolve("extracted");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(tarFile, outputDir, ArchiveFormat.TAR_GZ));

        assertThat(result.success()).isTrue();
        assertThat(Files.list(outputDir).count()).isGreaterThan(0);
    }

    @Test
    void extract_nonExistentFile_returnsFailure(@TempDir Path tempDir) throws IOException {
        Path fakeTar = tempDir.resolve("nonexistent.tar.gz");
        Path outputDir = tempDir.resolve("out");
        Files.createDirectory(outputDir);

        OperationResult result = handler.extract(new ExtractionRequest(fakeTar, outputDir, ArchiveFormat.TAR_GZ));

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
