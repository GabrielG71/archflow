package io.archflow.domain.model;

import io.archflow.domain.enums.ArchiveFormat;

import java.nio.file.Path;

/**
 * Holds all parameters required to perform a compression operation.
 */
public record CompressionRequest(
        Path inputPath,
        Path outputPath,
        ArchiveFormat format
) {}