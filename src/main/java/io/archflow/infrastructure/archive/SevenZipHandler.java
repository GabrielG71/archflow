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
 * Handles .7z compression and extraction using the system 7z or 7zz binary.
 * Tries "7z" first; falls back to "7zz" if not found (p7zip-full vs standalone).
 */
public class SevenZipHandler implements ArchiveHandler {

    private final CommandExecutor executor;

    public SevenZipHandler(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.SEVEN_ZIP;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        // 7z a <output.7z> <inputPath>
        List<String> command = List.of(
                resolveBinary(), "a",
                request.outputPath().toString(),
                request.inputPath().toString()
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo comprimido com sucesso: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao comprimir com 7z: " + result.stdout().trim());
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // 7z x <input.7z> -o<outputPath>  (no space between -o and path)
        List<String> command = List.of(
                resolveBinary(), "x",
                request.inputPath().toString(),
                "-o" + request.outputPath().toString(),
                "-y"
        );
        CommandResult result = executor.execute(command);
        if (result.isSuccess()) {
            return OperationResult.success("Arquivo extraído com sucesso em: " + request.outputPath());
        }
        return OperationResult.failure("Erro ao extrair com 7z: " + result.stdout().trim());
    }

    private String resolveBinary() {
        try {
            if (new ProcessBuilder("which", "7z").start().waitFor() == 0) return "7z";
            if (new ProcessBuilder("which", "7zz").start().waitFor() == 0) return "7zz";
        } catch (Exception ignored) {
        }
        return "7z";
    }
}
