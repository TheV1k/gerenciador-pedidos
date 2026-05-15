# Gerenciador de Pedidos

Sistema desenvolvido em Java com Spring Boot para gerenciamento de pedidos, produtos, categorias e fornecedores.

## 🚀 Funcionalidades

- Cadastro de produtos
- Cadastro de categorias
- Cadastro de fornecedores
- Cadastro de pedidos
- Busca de produtos por nome
- Contagem de produtos por categoria
- Ordenação de produtos por preço
- Relacionamento entre entidades com Spring Data JPA

---

# 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- IntelliJ IDEA

---

# 📂 Estrutura do projeto

```text
src/main/java
├── models
├── repository
├── service
└── Principal
```

---

# ⚙️ Como executar o projeto

## Pré-requisitos

- Java 21+
- Maven
- MySQL

## Clone o repositório

```bash
git clone https://github.com/TheV1k/gerenciador-pedidos.git
```

## Acesse o diretório

```bash
cd gerenciador-pedidos
```

## Configure o banco de dados

Edite o arquivo:

```text
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador_pedidos
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
```

## Execute a aplicação

```bash
mvn spring-boot:run
```

---

# 📌 Exemplos de parâmetros

| Parâmetro  | Tipo          | Exemplo                  |
| ---------- | ------------- | ------------------------ |
| peso       | INTEGER (1-3) | `?peso=3`                |
| dataInicio | DATE (ISO)    | `?dataInicio=2026-01-01` |
| dataFim    | DATE (ISO)    | `?dataFim=2026-12-31`    |

### Exemplo completo

```http
GET /api/atividades?peso=1&dataInicio=2026-01-01&dataFim=2026-12-31
```

---

# 📖 Exemplos de consultas Spring Data JPA

## Buscar produto por nome

```java
findByNomeContainingIgnoreCase(String nome)
```

## Buscar os 5 produtos mais baratos de uma categoria

```java
findTop5ByCategoriasNomeContainingIgnoreCaseOrderByPrecoAsc(String categoria)
```

## Contar produtos por categoria

```java
countByCategoriasNomeContainingIgnoreCase(String categoria)
```

---

# 📈 Melhorias futuras

- API REST
- Swagger/OpenAPI
- Testes unitários
- Docker
- JWT Authentication
- Paginação
- Relatórios
- Dashboard administrativo

---

# 🤝 Autor



[<img title="" src="https://avatars.githubusercontent.com/u/62910266?v=4" alt="Victor Moreira Ramos" style="zoom:50%;" data-align="inline">  
**Victor Moreira Ramos**](https://github.com/TheV1k)


