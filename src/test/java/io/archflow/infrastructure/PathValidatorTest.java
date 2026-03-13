package io.archflow.infrastructure;

import io.archflow.infrastructure.filesystem.PathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PathValidatorTest {

    private PathValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PathValidator();
    }

    @Test
    void exists_returnsTrueForExistingFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("arquivo.txt");
        Files.createFile(file);

        assertThat(validator.exists(file)).isTrue();
    }

    @Test
    void exists_returnsFalseForNonExistingPath(@TempDir Path tempDir) {
        Path ghost = tempDir.resolve("nao-existe.txt");

        assertThat(validator.exists(ghost)).isFalse();
    }

    @Test
    void exists_returnsTrueForExistingDirectory(@TempDir Path tempDir) throws IOException {
        Path dir = tempDir.resolve("subdir");
        Files.createDirectory(dir);

        assertThat(validator.exists(dir)).isTrue();
    }

    @Test
    void isReadable_returnsTrueForReadableFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("legivel.txt");
        Files.createFile(file);

        assertThat(validator.isReadable(file)).isTrue();
    }

    @Test
    void isReadable_returnsFalseForNonExistingPath(@TempDir Path tempDir) {
        Path ghost = tempDir.resolve("nao-existe.txt");

        assertThat(validator.isReadable(ghost)).isFalse();
    }

    @Test
    void isDirectory_returnsTrueForDirectory(@TempDir Path tempDir) throws IOException {
        Path dir = tempDir.resolve("pasta");
        Files.createDirectory(dir);

        assertThat(validator.isDirectory(dir)).isTrue();
    }

    @Test
    void isDirectory_returnsFalseForFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("arquivo.txt");
        Files.createFile(file);

        assertThat(validator.isDirectory(file)).isFalse();
    }

    @Test
    void isDirectory_returnsFalseForNonExistingPath(@TempDir Path tempDir) {
        Path ghost = tempDir.resolve("nao-existe");

        assertThat(validator.isDirectory(ghost)).isFalse();
    }

    @Test
    void destinationAlreadyExists_returnsTrueWhenFileExists(@TempDir Path tempDir) throws IOException {
        Path output = tempDir.resolve("saida.zip");
        Files.createFile(output);

        assertThat(validator.destinationAlreadyExists(output)).isTrue();
    }

    @Test
    void destinationAlreadyExists_returnsFalseWhenFileDoesNotExist(@TempDir Path tempDir) {
        Path output = tempDir.resolve("saida-nova.zip");

        assertThat(validator.destinationAlreadyExists(output)).isFalse();
    }
}
