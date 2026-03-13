package io.archflow.domain.model;

import io.archflow.domain.enums.ArchiveFormat;

import java.nio.file.Path;

/**
 * Holds all parameters required to perform an extraction operation.
 */
public record ExtractionRequest(
        Path inputPath,
        Path outputPath,
        ArchiveFormat format
) {}