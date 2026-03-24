package io.archflow.cli;

import io.archflow.application.usecase.CompressUseCase;
import io.archflow.application.usecase.DetectFormatUseCase;
import io.archflow.application.usecase.ExtractUseCase;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.infrastructure.archive.SevenZipHandler;
import io.archflow.infrastructure.archive.TarGzHandler;
import io.archflow.infrastructure.archive.XzHandler;
import io.archflow.infrastructure.archive.ZipHandler;
import io.archflow.infrastructure.filesystem.PathValidator;
import io.archflow.infrastructure.process.CommandExecutor;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.List;

/**
 * Entrypoint of the archflow CLI application.
 * Picocli handles argument parsing and wires up dependencies for the interactive flow.
 */
@Command(
        name = "archflow",
        mixinStandardHelpOptions = true,
        version = "archflow 0.1.0",
        description = "Utilitário interativo de compressão e extração de arquivos para Linux."
)
public class ArchflowCli implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ArchflowCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        CommandExecutor executor = new CommandExecutor();

        List<ArchiveHandler> handlers = List.of(
                new ZipHandler(executor),
                new TarGzHandler(executor),
                new SevenZipHandler(executor),
                new XzHandler(executor)
        );

        new ArchiveFlowRunner(
                new PathValidator(),
                new DetectFormatUseCase(),
                new CompressUseCase(handlers),
                new ExtractUseCase(handlers)
        ).run();
    }
}
