# archflow

Utilitário interativo de linha de comando para compressão e extração de arquivos no Linux.

---

## O que é

archflow é uma ferramenta CLI que guia o usuário de forma interativa pelo processo de compressão ou extração de arquivos. Basta informar o caminho de entrada e o archflow detecta o formato, pergunta o destino e executa a operação.

---

## Formatos suportados (MVP)

| Formato  | Compressão | Extração |
|----------|------------|----------|
| `.zip`   | ✓          | ✓        |
| `.tar.gz`| ✓          | ✓        |
| `.7z`    | ✓          | ✓        |
| `.xz`    | ✗          | ✓        |

---

## Limitações do MVP

- Apenas Linux
- Detecção de formato somente por extensão de arquivo
- Sem suporte a `.rar`
- Sem suporte a senhas ou criptografia
- Sem barra de progresso
- Sem multithreading
- `.xz` suporta apenas extração

---

## Pré-requisitos

Ferramentas de sistema necessárias:

```bash
sudo apt install zip unzip tar xz-utils p7zip-full
```

Java 21+ instalado.

---

## Como compilar

```bash
mvn clean package
```

O fat jar será gerado em `target/archflow-0.1.0-SNAPSHOT.jar`.

---

## Como executar

```bash
java -jar target/archflow-0.1.0-SNAPSHOT.jar
```

---

## Como rodar os testes

```bash
mvn test
```

---

## Arquitetura

```
io.archflow
├── cli              # fluxo interativo, prompts, entrypoint
├── application      # casos de uso, orquestração
├── domain           # modelos, enums, contratos, exceções
├── infrastructure   # handlers de formato, executor de processos, filesystem
└── config           # configuração da aplicação
```

---

## Roadmap

- [ ] Implementar detecção de formato por extensão
- [ ] Implementar PathValidator
- [ ] Implementar CommandExecutor com ProcessBuilder
- [ ] Implementar ZipHandler, TarGzHandler, SevenZipHandler, XzHandler
- [ ] Implementar fluxo interativo completo no CLI
- [ ] Adicionar testes unitários
- [ ] Suporte a `.tar.xz`
- [ ] Melhorias de UX no terminal

---

## Licença

MIT License. Veja [LICENSE](LICENSE).
