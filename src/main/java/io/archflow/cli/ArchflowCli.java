package io.archflow.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Entrypoint of the archflow CLI application.
 * Picocli handles argument parsing and delegates to the interactive flow.
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
        // TODO: start interactive flow via ArchiveFlowRunner
        System.out.println("archflow iniciando...");
    }
}
