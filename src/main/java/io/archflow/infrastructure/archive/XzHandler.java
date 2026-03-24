package io.archflow.infrastructure.archive;

import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.process.CommandExecutor;
import io.archflow.infrastructure.process.CommandResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles .xz extraction using the system xz binary.
 * Note: .xz compression is out of MVP scope.
 */
public class XzHandler implements ArchiveHandler {

    private final CommandExecutor executor;

    public XzHandler(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public ArchiveFormat supportedFormat() {
        return ArchiveFormat.XZ;
    }

    @Override
    public OperationResult compress(CompressionRequest request) {
        return OperationResult.failure("Compressão .xz não é suportada no MVP.");
    }

    @Override
    public OperationResult extract(ExtractionRequest request) {
        // xz -d -c reads inputPath and writes decompressed data to stdout.
        // We redirect stdout to the desired output file via executeWithOutputFile.
        String inputFilename = request.inputPath().getFileName().toString();
        // Strip the .xz extension to derive the output filename
        String outputFilename = inputFilename.endsWith(".xz")
                ? inputFilename.substring(0, inputFilename.length() - 3)
                : inputFilename + ".decompressed";

        Path outputFile = request.outputPath().resolve(outputFilename);

        try {
            Files.createDirectories(request.outputPath());
        } catch (IOException e) {
            return OperationResult.failure("Não foi possível criar o diretório de saída: " + e.getMessage());
        }

        List<String> command = List.of("xz", "-d", "-c", request.inputPath().toString());
        CommandResult result = executor.executeWithOutputFile(command, outputFile);

        if (result.isSuccess()) {
            return OperationResult.success("Arquivo extraído com sucesso: " + outputFile);
        }

        // Clean up incomplete output file on failure
        try {
            Files.deleteIfExists(outputFile);
        } catch (IOException ignored) {
        }

        return OperationResult.failure("Erro ao extrair com xz: " + result.stderr().trim());
    }
}
