package io.archflow.application;

import io.archflow.application.usecase.DetectFormatUseCase;
import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.exception.UnsupportedFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DetectFormatUseCaseTest {

    private DetectFormatUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DetectFormatUseCase();
    }

    @Test
    void execute_detectsZip() {
        assertThat(useCase.execute(Path.of("backup.zip"))).isEqualTo(ArchiveFormat.ZIP);
    }

    @Test
    void execute_detectsTarGz() {
        assertThat(useCase.execute(Path.of("backup.tar.gz"))).isEqualTo(ArchiveFormat.TAR_GZ);
    }

    @Test
    void execute_detectsSevenZip() {
        assertThat(useCase.execute(Path.of("backup.7z"))).isEqualTo(ArchiveFormat.SEVEN_ZIP);
    }

    @Test
    void execute_detectsXz() {
        assertThat(useCase.execute(Path.of("backup.xz"))).isEqualTo(ArchiveFormat.XZ);
    }

    @Test
    void execute_isCaseInsensitive() {
        assertThat(useCase.execute(Path.of("backup.ZIP"))).isEqualTo(ArchiveFormat.ZIP);
        assertThat(useCase.execute(Path.of("backup.TAR.GZ"))).isEqualTo(ArchiveFormat.TAR_GZ);
        assertThat(useCase.execute(Path.of("backup.7Z"))).isEqualTo(ArchiveFormat.SEVEN_ZIP);
    }

    @Test
    void execute_doesNotConfuseDotGzWithTarGz() {
        // .gz alone is not a supported format
        assertThatThrownBy(() -> useCase.execute(Path.of("backup.gz")))
                .isInstanceOf(UnsupportedFormatException.class);
    }

    @Test
    void execute_throwsForUnsupportedExtension() {
        assertThatThrownBy(() -> useCase.execute(Path.of("backup.rar")))
                .isInstanceOf(UnsupportedFormatException.class);
    }

    @Test
    void execute_throwsForFileWithNoExtension() {
        assertThatThrownBy(() -> useCase.execute(Path.of("backup")))
                .isInstanceOf(UnsupportedFormatException.class);
    }

    @Test
    void execute_worksWithFullAbsolutePath() {
        assertThat(useCase.execute(Path.of("/home/user/documentos/backup.zip")))
                .isEqualTo(ArchiveFormat.ZIP);
    }

    @Test
    void execute_worksWithNestedPath() {
        assertThat(useCase.execute(Path.of("projetos/2026/backup.tar.gz")))
                .isEqualTo(ArchiveFormat.TAR_GZ);
    }
}
