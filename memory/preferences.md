# Preferences

Preferências de colaboração, estilo de código e fluxo de trabalho.

---

## Idioma

- **Respostas**: português
- **Código**: inglês (nomes de classe, método, variável, comentários técnicos)
- **Mensagens ao usuário no terminal**: português (ex: "extensão não compatível")

---

## Commits

- Commits semânticos obrigatórios: `feat`, `fix`, `test`, `chore`, `docs`, `refactor`
- Escopo entre parênteses quando aplicável: `feat(domain)`, `test(infrastructure)`
- Um commit por unidade lógica — não agrupar coisas não relacionadas
- Ordem de commit por sessão: implementação → testes → memory update → commit
- **Antes de cada commit**: atualizar `memory/progress.md` com o que foi feito

---

## Estilo de código

- Sem comentários óbvios — só onde a lógica não é autoexplicativa
- Sem Javadoc em métodos triviais
- Records Java 21 para objetos de transferência de dados
- Métodos pequenos e com nome claro
- Sem abstrações prematuras

---

## Fluxo de trabalho

- Implementar → testar → commitar (nessa ordem)
- Rodar `mvn test` antes de qualquer commit de código
- Push é feito manualmente pelo usuário — nunca fazer push automático
- Não criar arquivos desnecessários (sem utils genéricas, sem helpers de uso único)

---

## Execução de comandos

- Rodar comandos diretamente sem pedir confirmação quando for operação segura e reversível
- Pedir confirmação antes de operações destrutivas ou irreversíveis
- Usar ferramentas dedicadas (Read, Edit, Write, Grep, Glob) em vez de bash quando possível
