package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;

/**
 * Handles .xz extraction using the system xz binary.
 * Note: .xz is extraction-only in the MVP.
 */
public class XzHandler implements ArchiveHandler {

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.XZ;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        // .xz compression is out of MVP scope
        throw new UnsupportedOperationException(".xz compression is not supported in the MVP");
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // TODO: build and execute: xz -dk <input.xz> then move to outputPath
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
