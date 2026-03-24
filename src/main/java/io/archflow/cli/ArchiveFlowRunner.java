package io.archflow.cli;

import io.archflow.application.usecase.CompressUseCase;
import io.archflow.application.usecase.DetectFormatUseCase;
import io.archflow.application.usecase.ExtractUseCase;
import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.exception.UnsupportedFormatException;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import io.archflow.infrastructure.filesystem.PathValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Drives the interactive CLI session.
 * Prompts the user step by step and delegates work to use cases.
 */
public class ArchiveFlowRunner {

    private final Scanner scanner;
    private final PathValidator pathValidator;
    private final DetectFormatUseCase detectFormat;
    private final CompressUseCase compressUseCase;
    private final ExtractUseCase extractUseCase;

    public ArchiveFlowRunner(
            PathValidator pathValidator,
            DetectFormatUseCase detectFormat,
            CompressUseCase compressUseCase,
            ExtractUseCase extractUseCase
    ) {
        this.scanner = new Scanner(System.in);
        this.pathValidator = pathValidator;
        this.detectFormat = detectFormat;
        this.compressUseCase = compressUseCase;
        this.extractUseCase = extractUseCase;
    }

    public void run() {
        System.out.println("=== archflow ===");
        System.out.print("Informe o caminho do arquivo ou diretório: ");
        String inputStr = scanner.nextLine().trim();

        if (inputStr.isEmpty()) {
            System.out.println("Caminho não informado. Encerrando.");
            return;
        }

        Path inputPath = Path.of(inputStr);

        if (!pathValidator.exists(inputPath)) {
            System.out.println("Caminho não encontrado: " + inputPath);
            return;
        }
        if (!pathValidator.isReadable(inputPath)) {
            System.out.println("Sem permissão de leitura: " + inputPath);
            return;
        }

        if (pathValidator.isDirectory(inputPath)) {
            runCompressionFlow(inputPath);
        } else {
            try {
                ArchiveFormat format = detectFormat.execute(inputPath);
                System.out.println("Formato detectado: " + formatLabel(format));
                runExtractionFlow(inputPath, format);
            } catch (UnsupportedFormatException e) {
                System.out.println("extensão não compatível");
                System.out.print("Deseja comprimir este arquivo? [s/N]: ");
                String answer = scanner.nextLine().trim();
                if (answer.equalsIgnoreCase("s")) {
                    runCompressionFlow(inputPath);
                }
            }
        }
    }

    private void runCompressionFlow(Path inputPath) {
        System.out.println("\nEscolha o formato de saída:");
        System.out.println("  [1] .zip");
        System.out.println("  [2] .tar.gz");
        System.out.println("  [3] .7z");
        System.out.print("Opção: ");
        String choice = scanner.nextLine().trim();

        ArchiveFormat format = switch (choice) {
            case "1" -> ArchiveFormat.ZIP;
            case "2" -> ArchiveFormat.TAR_GZ;
            case "3" -> ArchiveFormat.SEVEN_ZIP;
            default -> null;
        };

        if (format == null) {
            System.out.println("Opção inválida. Encerrando.");
            return;
        }

        System.out.print("\nInforme o diretório de saída: ");
        String outputDirStr = scanner.nextLine().trim();
        System.out.print("Informe o nome do arquivo (sem extensão): ");
        String outputName = scanner.nextLine().trim();

        String extension = switch (format) {
            case ZIP -> ".zip";
            case TAR_GZ -> ".tar.gz";
            case SEVEN_ZIP -> ".7z";
            default -> "";
        };

        Path outputPath = Path.of(outputDirStr).resolve(outputName + extension);

        if (pathValidator.destinationAlreadyExists(outputPath)) {
            System.out.println("\nAtenção: o arquivo " + outputPath + " já existe.");
            System.out.print("Deseja sobrescrever? [s/N]: ");
            String answer = scanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("s")) {
                System.out.println("Operação cancelada.");
                return;
            }
        }

        try {
            Files.createDirectories(outputPath.getParent());
        } catch (IOException e) {
            System.out.println("Não foi possível criar o diretório de saída: " + e.getMessage());
            return;
        }

        System.out.println("\nComprimindo...");
        OperationResult result = compressUseCase.execute(
                new CompressionRequest(inputPath, outputPath, format)
        );
        printResult(result);
    }

    private void runExtractionFlow(Path inputPath, ArchiveFormat format) {
        System.out.print("\nInforme o diretório de saída: ");
        String outputDirStr = scanner.nextLine().trim();
        Path outputDir = Path.of(outputDirStr);

        if (pathValidator.destinationAlreadyExists(outputDir)) {
            System.out.println("\nAtenção: o diretório " + outputDir + " já existe.");
            System.out.print("Deseja continuar e extrair para este diretório? [s/N]: ");
            String answer = scanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("s")) {
                System.out.println("Operação cancelada.");
                return;
            }
        }

        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.out.println("Não foi possível criar o diretório de saída: " + e.getMessage());
            return;
        }

        System.out.println("\nExtraindo...");
        OperationResult result = extractUseCase.execute(
                new ExtractionRequest(inputPath, outputDir, format)
        );
        printResult(result);
    }

    private void printResult(OperationResult result) {
        if (result.success()) {
            System.out.println("\n[OK] " + result.message());
        } else {
            System.out.println("\n[ERRO] " + result.message());
        }
    }

    private String formatLabel(ArchiveFormat format) {
        return switch (format) {
            case ZIP -> ".zip";
            case TAR_GZ -> ".tar.gz";
            case SEVEN_ZIP -> ".7z";
            case XZ -> ".xz";
        };
    }
}
