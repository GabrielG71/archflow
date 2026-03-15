# Constraints

Limitações e restrições que devem ser respeitadas durante o desenvolvimento.

---

## Escopo do MVP — não implementar

- `.rar` (binário `unrar` não é livre em todas as distros)
- `.tar.xz` (fora do MVP)
- Suporte a Windows ou macOS
- Detecção de formato por magic bytes (apenas extensão)
- Senhas ou criptografia
- Barra de progresso
- Multithreading
- TUI avançada
- Compressão `.xz` (apenas extração é suportada)

---

## Dependências

- Manter dependências mínimas: apenas Picocli, SLF4J, Logback, JUnit 5, AssertJ
- Não adicionar frameworks desnecessários para CLI (sem Spring, sem Quarkus)
- Não reimplementar engines de compressão nativas em Java — usar binários de sistema

---

## Arquitetura

- Não misturar responsabilidades entre camadas:
  - `cli` não faz IO de arquivo
  - `domain` não conhece infraestrutura
  - `infrastructure` não faz prompts ao usuário
- Não criar god classes
- Não adicionar abstrações para uso único

---

## Segurança básica

- Nunca sobrescrever arquivo silenciosamente — sempre confirmar com o usuário
- Validar paths antes de qualquer operação
- Centralizar lógica de overwrite em um único lugar

---

## Testes

- Focar em testes unitários significativos antes de testes de integração pesados
- Não mockar filesystem — usar `@TempDir` do JUnit 5
- Não mockar ProcessBuilder nos testes de handler — testar com binários reais do sistema
