# Gerenciador de Pedidos

Sistema desenvolvido em Java com Spring Boot para gerenciamento de fornecedores, categorias, produtos e pedidos, permitindo o controle completo do processo de compras e a geração de relatórios em PDF.

## 🚀 Funcionalidades

* Cadastro de categorias
* Cadastro de fornecedores
* Cadastro de produtos
* Cadastro de pedidos
* Consulta de pedidos por ID
* Listagem de pedidos cadastrados
* Atualização de pedidos
* Exclusão de registros
* Geração de PDF de pedidos
* Validação de regras de negócio
* Tratamento global de exceções
* Documentação da API com Swagger/OpenAPI
* Cobertura de testes unitários para a camada web (Controllers) e regras de negócio
---

# 🛠️ Tecnologias utilizadas

* **Linguagem & Framework:** Java 21, Spring Boot 3
* **Persistência:** Spring Data JPA, Hibernate, PostgreSQL
* **Testes:** JUnit 5, Mockito, MockMvc
* **Gerenciador de Dependências:** Maven
* **Documentação & Ferramentas:** Swagger / OpenAPI, Lombok, iText PDF, IntelliJ IDEA

---

# 📂 Estrutura do projeto

```text
src
├── main
│   ├── java/br/com/alura/exercicios/gerenciador_pedidos
│   │   ├── controller
│   │   ├── dto
│   │   ├── exceptions
│   │   ├── models
│   │   ├── repository
│   │   ├── service
│   │   └── GerenciadorPedidosApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java/br/com/alura/exercicios/gerenciador_pedidos
        ├── controller
        ├── service
        ├── validacoes
        └── GerenciadorPedidosApplicationTests.java
```

---

# 🏛️ Arquitetura da aplicação

O projeto segue o padrão de arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

### Camadas

| Camada     | Responsabilidade                                |
| ---------- | ----------------------------------------------- |
| Controller | Receber requisições HTTP e retornar respostas   |
| Service    | Implementar regras de negócio                   |
| Repository | Acesso ao banco de dados                        |
| DTO        | Transferência de dados entre cliente e servidor |
| Exception  | Tratamento global de erros                      |

---

# 📊 Modelo de domínio

```text
Fornecedor
     │
     ├── Produtos
     │
Pedido
     │
     ├── ItemPedido
     │        │
     │        └── Produto
     │
     └── Fornecedor

Produto
     │
     └── Categoria
```

---

# ⚙️ Como executar o projeto

## Pré-requisitos

* Java 21+
* Maven
* PostgreSQL

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
spring.datasource.url=jdbc:postgresql://localhost:5432/gerenciador_pedidos
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Execute a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```
## 🧪 Como executar os testes

A aplicação conta com testes unitários utilizando JUnit 5, Mockito e MockMvc para garantir a integridade dos endpoints e das regras de negócio.

Para rodar toda a suíte de testes, execute no terminal:

```
mvn test
```
Os cenários validados incluem:

Criação, consulta, atualização e remoção de registros.

Retornos de status HTTP corretos (200 OK, 201 Created, 204 No Content, 404 Not Found).

Comportamento e chamadas simuladas de serviços através de Mocks.

---

# 📖 Documentação da API

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação completa dos endpoints estará disponível através do Swagger.

---

# 📌 Exemplo de cadastro de fornecedor

## Requisição

```http
POST /fornecedores
```

```json
{
  "nome": "Tech Distribuidora",
  "cnpj": "12.345.678/0001-90",
  "email": "contato@tech.com",
  "endereco": "Rua das Empresas, 100"
}
```

---

# 📌 Exemplo de cadastro de produto

## Requisição

```http
POST /produtos
```

```json
{
  "nome": "Notebook Dell",
  "preco": 4500.00,
  "categoria": "Informática",
  "fornecedor": "Tech Distribuidora"
}
```

---

# 📌 Exemplo de cadastro de pedido

## Requisição

```http
POST /pedidos
```

```json
{
  "nomeFornecedor": "Tech Distribuidora",
  "itemPedido": [
    {
      "produto": "Notebook Dell",
      "quantidade": 5
    },
    {
      "produto": "Mouse Logitech",
      "quantidade": 10
    }
  ],
  "dataPedido": "2026-06-05"
}
```

---

# ⚠️ Tratamento de exceções

A aplicação possui tratamento global de exceções através do `GlobalExceptionHandler`.

Exceções implementadas:

* ResourceNotFoundException
* DuplicateResourceException
* BusinessRuleException
* PrecoInvalidoException

Exemplo de retorno:

```json
{
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Produto não encontrado"
}
```

---

# 📄 Geração de PDF

O sistema permite gerar um relatório em PDF contendo:

* Dados do fornecedor
* Informações do pedido
* Lista de produtos
* Quantidades
* Preço unitário
* Valor total

---

# 📈 Melhorias futuras

* Testes de integração
* Docker e Docker Compose
* Autenticação JWT
* Controle de estoque
* Histórico de status dos pedidos
* GitHub Actions (CI/CD)
* Paginação e filtros avançados

---

# 📄 Licença

![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

# 🤝 Autor

<p align="center">
  <a href="https://github.com/TheV1k">
    <img src="https://avatars.githubusercontent.com/u/62910266?s=400&v=4" width="120" alt="Victor Moreira Ramos"><br>
    <b>Victor Moreira Ramos</b>
  </a>
</p>
<p align="center"> Desenvolvedor Backend Java | Spring Boot | PostgreSQL</p>
