# Decisions

Decisões técnicas e arquiteturais tomadas no projeto. Registra o quê, o porquê e quando.

---

## Linguagem e build

**Java 21 + Maven**
Java 21 por suporte a records (imutabilidade sem boilerplate), sealed classes no futuro, e LTS ativo. Maven por ser o padrão consolidado para projetos Java com ecossistema estável.

---

## groupId

**`io.archflow`**
Nome de domínio reverso sem empresa atrelada. Adequado para open source independente.

---

## Fat JAR via maven-shade-plugin

Gera um único `.jar` com todas as dependências embutidas. O usuário executa com `java -jar archflow.jar` sem configurar classpath. Alternativa (`maven-assembly-plugin`) foi descartada por ser mais verbosa.

---

## Detecção de formato apenas por extensão

Conforme CLAUDE.md: format detection is based **only on extension**. Sem magic bytes no MVP. A detecção por extensão é suficiente para o escopo atual e evita complexidade desnecessária.

`.tar.gz` é verificado antes de `.gz` para evitar match parcial incorreto.

---

## Binários de sistema via ProcessBuilder

Java delega compressão/extração para `zip`, `unzip`, `tar`, `7z`, `xz`. Motivo: ferramentas de sistema são mais confiáveis, testadas e performáticas do que reimplementar em Java puro no MVP. Java cuida de orquestração, validação e fluxo.

---

## Records Java 21 para modelos

`CompressionRequest`, `ExtractionRequest`, `CommandResult` e `OperationResult` são records. Imutáveis por design, sem setters, sem construtor manual. Corretos para objetos de transferência de dados.

---

## Logging

SLF4J como abstração + Logback como implementação. Logs vão exclusivamente para arquivo (`logs/archflow.log`) com rotação diária de 30 dias. O console recebe apenas `ERROR` para não poluir a CLI interativa. Nenhuma mensagem de progresso de operação vai para o log — só metadados da operação (tipo, paths, comando, resultado).

---

## Picocli com annotation processor

Configurado no `maven-compiler-plugin` para gerar metadados de reflexão em tempo de compilação. Isso melhora performance, reduz footprint e abre caminho para GraalVM native image no futuro sem reconfiguração.

---

## Handlers por formato

Cada formato tem seu próprio handler isolado implementando `ArchiveHandler`. Isso permite:
- adicionar novos formatos sem tocar em código existente
- testar cada handler de forma independente
- trocar a estratégia de execução por handler sem afetar os outros

---

## Sem suporte a .rar no MVP

`.rar` requer o binário `unrar` que não é livre/open source em todas as distribuições. Fora do escopo do MVP.

---

## XzHandler: apenas extração no MVP

`.xz` puro é formato de stream, não de arquivo agrupado. Compressão de diretórios com `.xz` normalmente usa `.tar.xz`, que está fora do MVP. Apenas extração de arquivos `.xz` é suportada.
