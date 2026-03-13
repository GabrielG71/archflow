package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;

/**
 * Handles .tar.gz compression and extraction using the system tar binary.
 */
public class TarGzHandler implements ArchiveHandler {

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.TAR_GZ;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        // TODO: build and execute: tar -czf <output.tar.gz> -C <parent> <inputName>
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // TODO: build and execute: tar -xzf <input.tar.gz> -C <outputPath>
        throw new UnsupportedOperationException("Not implemented yet");
    }
}