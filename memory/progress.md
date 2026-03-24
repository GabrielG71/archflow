# Progress

Estado atual do desenvolvimento. Deve ser atualizado antes de cada commit.

---

## Concluído

| Módulo | Arquivo | Testes | Commit |
|--------|---------|--------|--------|
| `.gitignore` | `.gitignore` | — | `09d7ff1` |
| Build config | `pom.xml` | — | `45c82c4` |
| Logging config | `src/main/resources/logback.xml` | — | `358454e` |
| Enums do domínio | `domain/enums/*` | — | `1228f2c` |
| Models do domínio | `domain/model/*` | — | `47ad854` |
| Interface `ArchiveHandler` | `domain/handler/ArchiveHandler.java` | — | `f52eaab` |
| Exceptions do domínio | `domain/exception/*` | — | `c7105c8` |
| `CommandExecutor` | `infrastructure/process/CommandExecutor.java` | ✅ 7 testes | — |
| `PathValidator` | `infrastructure/filesystem/PathValidator.java` | ✅ 10 testes | `8d73ae1` + `934ef74` |
| `DetectFormatUseCase` | `application/usecase/DetectFormatUseCase.java` | ✅ 10 testes | `11b69ab` + `120ce29` |
| `ZipHandler` | `infrastructure/archive/ZipHandler.java` | ✅ 5 testes | `ee7fa9f` |
| `TarGzHandler` | `infrastructure/archive/TarGzHandler.java` | ✅ 5 testes | `ee7fa9f` |
| `SevenZipHandler` | `infrastructure/archive/SevenZipHandler.java` | ✅ 5 testes | `ee7fa9f` |
| `XzHandler` | `infrastructure/archive/XzHandler.java` | ✅ 5 testes | `ee7fa9f` |
| `CompressUseCase` | `application/usecase/CompressUseCase.java` | ✅ 4 testes | `4ba61fb` |
| `ExtractUseCase` | `application/usecase/ExtractUseCase.java` | ✅ 4 testes | `4ba61fb` |
| `ArchiveFlowRunner` | `cli/ArchiveFlowRunner.java` | — | `157e974` |
| `ArchflowCli` (conectado) | `cli/ArchflowCli.java` | — | `157e974` |
| Documentação | `README.md` + `LICENSE` | — | `c0da0dc` |
| Diretrizes do projeto | `CLAUDE.md` | — | `c8761f2` |

---

## Em andamento

Nada em andamento no momento — MVP completo e funcional.

---

## Próximos passos (melhorias pós-MVP)

1. Testes de integração end-to-end (comprime + extrai e valida conteúdo)
2. Testes do `ArchiveFlowRunner` com Scanner simulado
3. Build do fat JAR e teste manual via `java -jar`
4. Publicação de release no GitHub

---

## Contagem

- ✅ MVP completo: todos os módulos implementados
- Total de testes passando: **55/55**
