package io.archflow.application;

import io.archflow.application.usecase.CompressUseCase;
import io.archflow.domain.enums.ArchiveFormat;
import io.archflow.domain.handler.ArchiveHandler;
import io.archflow.domain.model.CompressionRequest;
import io.archflow.domain.model.ExtractionRequest;
import io.archflow.domain.model.OperationResult;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompressUseCaseTest {

    @Test
    void execute_knownFormat_delegatesToHandler() {
        ArchiveHandler zipHandler = new StubHandler(ArchiveFormat.ZIP, OperationResult.success("ok"));
        CompressUseCase useCase = new CompressUseCase(List.of(zipHandler));

        OperationResult result = useCase.execute(
                new CompressionRequest(Path.of("input"), Path.of("output.zip"), ArchiveFormat.ZIP)
        );

        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo("ok");
    }

    @Test
    void execute_unknownFormat_returnsFailure() {
        CompressUseCase useCase = new CompressUseCase(List.of());

        OperationResult result = useCase.execute(
                new CompressionRequest(Path.of("input"), Path.of("output.zip"), ArchiveFormat.ZIP)
        );

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("não suportado");
    }

    @Test
    void execute_multipleHandlers_routesCorrectly() {
        ArchiveHandler zipHandler = new StubHandler(ArchiveFormat.ZIP, OperationResult.success("zip ok"));
        ArchiveHandler tarHandler = new StubHandler(ArchiveFormat.TAR_GZ, OperationResult.success("tar ok"));
        CompressUseCase useCase = new CompressUseCase(List.of(zipHandler, tarHandler));

        OperationResult result = useCase.execute(
                new CompressionRequest(Path.of("input"), Path.of("output.tar.gz"), ArchiveFormat.TAR_GZ)
        );

        assertThat(result.message()).isEqualTo("tar ok");
    }

    @Test
    void execute_handlerReturnsFailure_propagatesFailure() {
        ArchiveHandler failingHandler = new StubHandler(ArchiveFormat.ZIP, OperationResult.failure("disk full"));
        CompressUseCase useCase = new CompressUseCase(List.of(failingHandler));

        OperationResult result = useCase.execute(
                new CompressionRequest(Path.of("input"), Path.of("output.zip"), ArchiveFormat.ZIP)
        );

        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("disk full");
    }

    // Minimal stub handler used only in this test class
    private static class StubHandler implements ArchiveHandler {
        private final ArchiveFormat format;
        private final OperationResult compressResult;

        StubHandler(ArchiveFormat format, OperationResult compressResult) {
            this.format = format;
            this.compressResult = compressResult;
        }

        @Override
        public ArchiveFormat supportedFormat() { return format; }

        @Override
        public OperationResult compress(CompressionRequest request) { return compressResult; }

        @Override
        public OperationResult extract(ExtractionRequest request) { return OperationResult.failure("N/A"); }
    }
}
