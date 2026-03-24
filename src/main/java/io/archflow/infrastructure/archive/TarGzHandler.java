package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.process.CommandExecutor;
import io.archflow.infrastructure.process.CommandResult;

import java.nio.file.Path;
import java.util.List;

/**
 * Handles .tar.gz compression and extraction using the system tar binary.
 */
public class TarGzHandler implements ArchiveHandler {

    private final CommandExecutor executor;

    public TarGzHandler(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.TAR_GZ;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        Path inputPath = request.inputPath();
        // Use -C <parent> <name> so the archive does not embed the full absolute path
        Path parent = inputPath.getParent() != null ? inputPath.getParent() : Path.of(".");
        String inputName = inputPath.getFileName().toString();

        List<String> command = List.of(
                "tar", "-czf",
                request.outputPath().toString(),
                "-C", parent.toString(),
                inputName
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo comprimido com sucesso: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao comprimir com tar: " + result.stdout().trim());
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        List<String> command = List.of(
                "tar", "-xzf",
                request.inputPath().toString(),
                "-C", request.outputPath().toString()
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo extraído com sucesso em: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao extrair com tar: " + result.stdout().trim());
    }
}