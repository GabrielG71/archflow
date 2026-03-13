package io.archflow.domain.handler;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;

/**
 * Contract for all archive format handlers.
 * Each supported format must implement this interface.
 */
public interface ArchiveHandler {

    ArchiveFormat supportedFormat();

    OperationResult compress(CompressionRequest request);

    OperationResult extract(ExtractionRequest request);
}