# Architecture

Mapa estrutural do projeto para navegação rápida.

---

## Pacotes e responsabilidades

```
io.archflow
├── cli/
│   ├── ArchflowCli.java          → entrypoint Picocli (@Command, main)
│   └── ArchiveFlowRunner.java    → fluxo interativo com Scanner (prompts, overwrite, resultado)
│
├── application/
│   └── usecase/
│       ├── DetectFormatUseCase.java  → detecta ArchiveFormat pela extensão do arquivo
│       ├── CompressUseCase.java      → orquestra compressão: valida → handler → executa
│       └── ExtractUseCase.java       → orquestra extração:    valida → handler → executa
│
├── domain/
│   ├── enums/
│   │   ├── ArchiveFormat.java    → ZIP, TAR_GZ, SEVEN_ZIP, XZ
│   │   ├── OperationType.java    → COMPRESS, EXTRACT
│   │   ├── InputType.java        → FILE, DIRECTORY
│   │   └── OverwriteDecision.java → CONFIRM, CANCEL
│   ├── model/
│   │   ├── CompressionRequest.java → (inputPath, outputPath, format)
│   │   ├── ExtractionRequest.java  → (inputPath, outputPath, format)
│   │   └── OperationResult.java    → (success, message) + factories success()/failure()
│   ├── handler/
│   │   └── ArchiveHandler.java  → interface: supportedFormat(), compress(), extract()
│   └── exception/
│       ├── UnsupportedFormatException.java  → extensão não reconhecida
│       └── OperationFailedException.java    → falha na execução do comando
│
├── infrastructure/
│   ├── archive/
│   │   ├── ZipHandler.java       → zip -r / unzip -d
│   │   ├── TarGzHandler.java     → tar -czf / tar -xzf
│   │   ├── SevenZipHandler.java  → 7z a / 7z x
│   │   └── XzHandler.java        → xz -dk (extração apenas)
│   ├── process/
│   │   ├── CommandExecutor.java  → ProcessBuilder wrapper
│   │   └── CommandResult.java    → (exitCode, stdout, stderr) + isSuccess()
│   └── filesystem/
│       └── PathValidator.java    → exists, isReadable, isDirectory, destinationAlreadyExists
│
└── config/                       → reservado para configuração futura
```

---

## Fluxo de execução esperado

```
ArchflowCli.main()
    └── ArchiveFlowRunner.run()
            ├── pede input path ao usuário
            ├── PathValidator.exists() + isReadable()
            ├── DetectFormatUseCase.execute(path)
            │       └── retorna ArchiveFormat ou lança UnsupportedFormatException
            ├── determina OperationType (arquivo = extract, dir = compress)
            ├── pede output path e nome final ao usuário
            ├── PathValidator.destinationAlreadyExists()
            │       └── se sim: pede confirmação (OverwriteDecision)
            ├── monta CompressionRequest ou ExtractionRequest
            ├── CompressUseCase.execute() ou ExtractUseCase.execute()
            │       └── resolve handler por formato
            │       └── handler.compress() ou handler.extract()
            │               └── CommandExecutor.execute(List<String> command)
            │                       └── ProcessBuilder → CommandResult
            └── imprime resultado ao usuário
```

---

## Dependências externas necessárias no sistema

| Binário | Pacote apt | Uso |
|---------|-----------|-----|
| `zip` | `zip` | compressão .zip |
| `unzip` | `unzip` | extração .zip |
| `tar` | `tar` (nativo) | compressão e extração .tar.gz |
| `7z` | `p7zip-full` | compressão e extração .7z |
| `xz` | `xz-utils` | extração .xz |

---

## Configuração de build

- **Java**: 21
- **Empacotamento**: fat JAR via `maven-shade-plugin`
- **Main class**: `io.archflow.cli.ArchflowCli`
- **Logs**: `logs/archflow.log` (criado em runtime, ignorado no git)
