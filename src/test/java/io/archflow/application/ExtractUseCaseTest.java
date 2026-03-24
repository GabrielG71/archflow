package io.archflow.application;

import io.archflow.application.usecase.ExtractUseCase;
import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtractUseCaseTest {

    @Test
    void execute_knownFormat_delegatesToHandler() {
        ArchiveHandler zipHandler = new StubHandler(ArchiveFormat.ZIP, OperationResult.success("extracted"));
        ExtractUseCase useCase = new ExtractUseCase(List.of(zipHandler));

        OperationResult result = useCase.execute(
                new ExtractionRequest(Path.of("archive.zip"), Path.of("outdir"), ArchiveFormat.ZIP)
        );

        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo("extracted");
    }

    @Test
    void execute_unknownFormat_returnsFailure() {
        ExtractUseCase useCase = new ExtractUseCase(List.of());

        OperationResult result = useCase.execute(
                new ExtractionRequest(Path.of("archive.zip"), Path.of("outdir"), ArchiveFormat.ZIP)
        );

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("não suportado");
    }

    @Test
    void execute_multipleHandlers_routesCorrectly() {
        ArchiveHandler zipHandler = new StubHandler(ArchiveFormat.ZIP, OperationResult.success("zip extracted"));
        ArchiveHandler xzHandler = new StubHandler(ArchiveFormat.XZ, OperationResult.success("xz extracted"));
        ExtractUseCase useCase = new ExtractUseCase(List.of(zipHandler, xzHandler));

        OperationResult result = useCase.execute(
                new ExtractionRequest(Path.of("archive.xz"), Path.of("outdir"), ArchiveFormat.XZ)
        );

        assertThat(result.message()).isEqualTo("xz extracted");
    }

    @Test
    void execute_handlerReturnsFailure_propagatesFailure() {
        ArchiveHandler failingHandler = new StubHandler(ArchiveFormat.TAR_GZ, OperationResult.failure("corrupt archive"));
        ExtractUseCase useCase = new ExtractUseCase(List.of(failingHandler));

        OperationResult result = useCase.execute(
                new ExtractionRequest(Path.of("archive.tar.gz"), Path.of("outdir"), ArchiveFormat.TAR_GZ)
        );

        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("corrupt archive");
    }

    private static class StubHandler implements ArchiveHandler {
        private final ArchiveFormat format;
        private final OperationResult extractResult;

        StubHandler(ArchiveFormat format, OperationResult extractResult) {
            this.format = format;
            this.extractResult = extractResult;
        }

        @Override
        public ArchiveFormat supportedFormat() { return format; }

        @Override
        public OperationResult compress(CompressionRequest request) { return OperationResult.failure("N/A"); }

        @Override
        public OperationResult extract(ExtractionRequest request) { return extractResult; }
    }
}
