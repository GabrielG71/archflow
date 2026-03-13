package io.archflow.application.usecase;

import io.archflow.domain.enums.ArchiveFormat;

import java.nio.file.Path;

/**
 * Detects the archive format of a given path based solely on its extension.
 * Throws UnsupportedFormatException if the extension is not recognized.
 */
public class DetectFormatUseCase {

    public ArchiveFormat execute(Path path) {
        // TODO: implement extension-based detection
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
