package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;

/**
 * Handles .7z compression and extraction using the system 7z / 7zz binary.
 */
public class SevenZipHandler implements ArchiveHandler {

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.SEVEN_ZIP;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        // TODO: build and execute: 7z a <output.7z> <inputPath>
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // TODO: build and execute: 7z x <input.7z> -o<outputPath>
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
