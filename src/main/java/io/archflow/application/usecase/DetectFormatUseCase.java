package io.archflow.application.usecase;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.exception.UnsupportedFormatException;

import java.nio.file.Path;

/**
 * Detects the archive format of a given path based solely on its extension.
 * Throws UnsupportedFormatException if the extension is not recognized.
 */
public class DetectFormatUseCase {

    public ArchiveFormat execute(Path path) {
        String filename = path.getFileName().toString().toLowerCase();

        if (filename.endsWith(".tar.gz")) {
            return ArchiveFormat.TAR_GZ;
        }
        if (filename.endsWith(".zip")) {
            return ArchiveFormat.ZIP;
        }
        if (filename.endsWith(".7z")) {
            return ArchiveFormat.SEVEN_ZIP;
        }
        if (filename.endsWith(".xz")) {
            return ArchiveFormat.XZ;
        }

        String extension = extractExtension(filename);
        throw new UnsupportedFormatException(extension);
    }

    private String extractExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : filename;
    }
}
