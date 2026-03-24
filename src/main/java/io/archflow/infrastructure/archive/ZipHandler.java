package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.process.CommandExecutor;
import io.archflow.infrastructure.process.CommandResult;

import java.util.List;

/**
 * Handles .zip compression and extraction using system binaries (zip / unzip).
 */
public class ZipHandler implements ArchiveHandler {

    private final CommandExecutor executor;

    public ZipHandler(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.ZIP;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        List<String> command = List.of(
                "zip", "-r",
                request.outputPath().toString(),
                request.inputPath().toString()
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo comprimido com sucesso: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao comprimir com zip: " + result.stdout().trim());
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        List<String> command = List.of(
                "unzip",
                request.inputPath().toString(),
                "-d",
                request.outputPath().toString()
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo extraído com sucesso em: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao extrair com unzip: " + result.stdout().trim());
    }
}