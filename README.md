# Java Audit & Access Control

Sistema de controle de acesso, validação de permissões e auditoria de eventos desenvolvido em **Java puro**.

---

## Objetivo do Projeto

Simular um motor de auditoria corporativa capaz de:
1. Cadastrar colaboradores com diferentes níveis de privilégio (`Guest`, `Regular`, `Admin`).
2. Importar registros de eventos (logins, exportações de dados e alterações de configuração) a partir de um arquivo `.csv`.
3. Validar regras de segurança de forma polimórfica em cada perfil, bloqueando ações não autorizadas ou contas que excedam o limite de falhas de autenticação.
4. Exportar um relatório consolidado da auditoria (incluindo alertas e tentativas rejeitadas) em arquivo de texto na pasta `/out`.

---

## Arquitetura e Conceitos Aplicados

O projeto foi estruturado separando responsabilidades entre apresentação, domínio e serviços de I/O:

```text
src/
 ├── app/
 │    └── Program.java               # Interface CLI, upcasting de perfis e captura global de exceções
 ├── entities/
 │    ├── Employee.java              # Superclasse abstrata (Template Method para validação e bloqueio)
 │    ├── GuestUser.java             # Visitante com data de expiração e tolerância de 2 falhas
 │    ├── RegularEmployee.java       # Colaborador padrão (tolerância de 3 falhas, sem alterar configs)
 │    ├── AdminEmployee.java         # Administrador com validação de MFA (tolerância de 5 falhas)
 │    ├── Department.java            # Departamento associado ao colaborador
 │    ├── AuditLog.java              # Superclasse abstrata dos eventos de auditoria
 │    ├── LoginLog.java              # Evento de autenticação (sucesso/falha e dispositivo)
 │    ├── DataExportLog.java         # Evento de extração de dados (eleva risco se > 10.000 registros)
 │    ├── ConfigChangeLog.java       # Evento de alteração de parâmetros do sistema
 │    ├── enums/
 │    │    ├── ActionType.java       # Tipos de ação auditadas
 │    │    └── RiskLevel.java        # Classificação de risco (LOW, MEDIUM, HIGH, CRITICAL)
 │    └── exceptions/
 │         └── AuditException.java   # Exceção personalizada de domínio
 └── services/
      └── AuditFileService.java      # Leitura de CSV e geração de relatórios com try-with-resources
