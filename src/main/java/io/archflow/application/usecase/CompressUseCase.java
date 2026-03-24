package io.archflow.application.usecase;

import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Orchestrates the compression flow: resolves the correct handler by format and delegates execution.
 */
public class CompressUseCase {

    private static final Logger log = LoggerFactory.getLogger(CompressUseCase.class);

    private final Map<io.archflow.domain.enums.ArchiveFormat, ArchiveHandler> handlers;

    public CompressUseCase(List<ArchiveHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ArchiveHandler::supportedFormat, h -> h));
    }

    public OperationResult execute(CompressionRequest request) {
        log.info("Compression requested: format={}, input={}, output={}",
                request.format(), request.inputPath(), request.outputPath());

        ArchiveHandler handler = handlers.get(request.format());
        if (handler == null) {
            log.warn("No handler found for format: {}", request.format());
            return OperationResult.failure("Formato não suportado para compressão: " + request.format());
        }

        log.info("Using handler: {}", handler.getClass().getSimpleName());
        OperationResult result = handler.compress(request);
        log.info("Compression result: success={}, message={}", result.success(), result.message());
        return result;
    }
}
