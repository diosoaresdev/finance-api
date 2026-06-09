# Finance API

API REST para controle de finanças pessoais desenvolvida com Java 21 e Spring Boot 3.5.

## Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Docker
- OpenCSV

## Funcionalidades

- Autenticação com JWT (registro e login)
- Gerenciamento de categorias (receitas e despesas)
- Registro de transações financeiras
- Relatório de resumo por período
- Exportação de transações em CSV

## Como rodar o projeto

### Pré-requisitos
- Java 21
- Docker

### 1. Clone o repositório
```bash
git clone https://github.com/seuusuario/finance-api.git
cd finance-api
```

### 2. Suba o banco de dados
```bash
docker compose up -d
```

### 3. Rode a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## Endpoints

### Auth
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /auth/register | Cadastro de usuário |
| POST | /auth/login | Login e geração de token |

### Categorias
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /categories | Listar categorias |
| POST | /categories | Criar categoria |
| DELETE | /categories/{id} | Deletar categoria |

### Transações
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /transactions | Listar transações |
| POST | /transactions | Criar transação |
| PUT | /transactions/{id} | Editar transação |
| DELETE | /transactions/{id} | Deletar transação |

### Relatórios
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /reports/summary?start=YYYY-MM-DD&end=YYYY-MM-DD | Resumo financeiro por período |
| GET | /reports/export/csv | Exportar transações em CSV |

## Autenticação

Todas as rotas exceto `/auth/**` exigem token JWT no header:

```
Authorization: Bearer {token}
```