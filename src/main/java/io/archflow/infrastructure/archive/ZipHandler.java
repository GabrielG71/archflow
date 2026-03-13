package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;

/**
 * Handles .zip compression and extraction using system binaries (zip / unzip).
 */
public class ZipHandler implements ArchiveHandler {

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.ZIP;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        // TODO: build and execute: zip -r <output.zip> <inputPath>
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // TODO: build and execute: unzip <input.zip> -d <outputPath>
        throw new UnsupportedOperationException("Not implemented yet");
    }
}