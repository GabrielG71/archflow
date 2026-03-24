package io.archflow.application.usecase;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Orchestrates the extraction flow: resolves the correct handler by format and delegates execution.
 */
public class ExtractUseCase {

    private static final Logger log = LoggerFactory.getLogger(ExtractUseCase.class);

    private final Map<ArchiveFormat, ArchiveHandler> handlers;

    public ExtractUseCase(List<ArchiveHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ArchiveHandler::supportedFormat, h -> h));
    }

    public OperationResult execute(ExtractionRequest request) {
        log.info("Extraction requested: format={}, input={}, output={}",
                request.format(), request.inputPath(), request.outputPath());

        ArchiveHandler handler = handlers.get(request.format());
        if (handler == null) {
            log.warn("No handler found for format: {}", request.format());
            return OperationResult.failure("Formato não suportado para extração: " + request.format());
        }

        log.info("Using handler: {}", handler.getClass().getSimpleName());
        OperationResult result = handler.extract(request);
        log.info("Extraction result: success={}, message={}", result.success(), result.message());
        return result;
    }
}
