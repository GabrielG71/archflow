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
| Handlers (stubs) | `infrastructure/archive/*` | — | `60147ce` |
| `CompressUseCase` (stub) | `application/usecase/CompressUseCase.java` | — | `c92696e` |
| `ExtractUseCase` (stub) | `application/usecase/ExtractUseCase.java` | — | `c92696e` |
| CLI entrypoint (stub) | `cli/ArchflowCli.java` + `cli/ArchiveFlowRunner.java` | — | `71dd644` |
| Documentação | `README.md` + `LICENSE` | — | `c0da0dc` |
| Diretrizes do projeto | `CLAUDE.md` | — | `c8761f2` |

---

## Em andamento

Nada em andamento no momento.

---

## Próximos passos (ordem obrigatória)

1. ~~**`CommandExecutor`** — implementar ProcessBuilder wrapper com captura de stdout/stderr e exit code~~
2. **`ZipHandler`** — implementar compress (`zip -r`) e extract (`unzip -d`)
3. **`TarGzHandler`** — implementar compress (`tar -czf`) e extract (`tar -xzf`)
4. **`SevenZipHandler`** — implementar compress e extract via `7z`
5. **`XzHandler`** — implementar apenas extract via `xz -dk`
6. **`CompressUseCase`** — orquestrar: validar path → detectar handler → executar → retornar resultado
7. **`ExtractUseCase`** — orquestrar: validar path → detectar handler → executar → retornar resultado
8. **`ArchiveFlowRunner`** — fluxo interativo completo: prompts, overwrite check, resultado

---

## Contagem

- ✅ Concluídos: 3 módulos com lógica real (`PathValidator`, `DetectFormatUseCase`, `CommandExecutor`)
- ⬜ Pendentes com lógica: 7 módulos
- Total de testes passando: **27/27**
