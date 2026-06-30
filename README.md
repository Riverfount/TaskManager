# Task Manager

API REST simples para gerenciamento de tarefas (CRUD), construída com Spring Boot. O projeto permite criar, listar, atualizar e remover tarefas, com validação de dados e documentação interativa via Swagger/OpenAPI.

## Sumário

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Como clonar e executar](#como-clonar-e-executar)
- [Endpoints da API](#endpoints-da-api)
- [Modelo de dados](#modelo-de-dados)
- [Documentação (Swagger) e banco H2](#documentação-swagger-e-banco-h2)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como contribuir](#como-contribuir)

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.6** (Web MVC, Data JPA, Validation)
- **H2 Database** (banco em memória)
- **springdoc-openapi** (Swagger UI)
- **Maven** (com Maven Wrapper incluído)

## Pré-requisitos

- **JDK 21** ou superior instalado e configurado (`java -version`).
- **Git** para clonar o repositório.
- Maven **não** é necessário globalmente — o projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Como clonar e executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/Riverfount/TaskManager.git
   cd TaskManager
   ```

2. Execute a aplicação com o Maven Wrapper:

   **Linux / macOS**
   ```bash
   ./mvnw spring-boot:run
   ```

   **Windows**
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. A API ficará disponível em:

   ```
   http://localhost:8080
   ```

### Gerar o pacote (build)

```bash
./mvnw clean package
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar
```

> O banco de dados é H2 em memória (`create-drop`), ou seja, os dados são recriados a cada inicialização e perdidos ao encerrar a aplicação.

## Endpoints da API

Base path: `/v1/api/tasks`

| Método   | Rota                  | Descrição                          | Status de sucesso |
|----------|-----------------------|------------------------------------|-------------------|
| `GET`    | `/v1/api/tasks`       | Lista todas as tarefas             | `200 OK`          |
| `GET`    | `/v1/api/tasks/{id}`  | Busca uma tarefa pelo ID           | `200 OK`          |
| `POST`   | `/v1/api/tasks`       | Cria uma nova tarefa               | `201 Created`     |
| `PUT`    | `/v1/api/tasks/{id}`  | Atualiza uma tarefa existente      | `200 OK`          |
| `DELETE` | `/v1/api/tasks/{id}`  | Remove uma tarefa                  | `204 No Content`  |

### Exemplo — criar tarefa

```bash
curl -X POST http://localhost:8080/v1/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Estudar Spring Boot",
        "description": "Concluir o módulo de APIs REST",
        "status": "PENDING",
        "dueDate": "2026-07-15"
      }'
```

### Exemplo de resposta

```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Concluir o módulo de APIs REST",
  "dueDate": "2026-07-15",
  "status": "PENDING",
  "createdAt": "2026-06-30T12:00:00Z",
  "updatedAt": "2026-06-30T12:00:00Z"
}
```

## Modelo de dados

### Campos de uma tarefa (`Task`)

| Campo         | Tipo         | Regras                                                       |
|---------------|--------------|-------------------------------------------------------------|
| `id`          | Long         | Gerado automaticamente                                      |
| `title`       | String       | Obrigatório, máximo de 200 caracteres                       |
| `description` | String       | Máximo de 2000 caracteres                                   |
| `status`      | TaskStatus   | `PENDING`, `IN_PROGRESS` ou `DONE` (padrão: `PENDING`)      |
| `dueDate`     | LocalDate    | Não pode ser uma data no passado                            |
| `createdAt`   | Instant      | Preenchido automaticamente na criação                       |
| `updatedAt`   | Instant      | Atualizado automaticamente a cada modificação               |

### Status possíveis (`TaskStatus`)

- `PENDING` — pendente
- `IN_PROGRESS` — em andamento
- `DONE` — concluída

## Documentação (Swagger) e banco H2

Com a aplicação em execução:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI (JSON):** http://localhost:8080/v3/api-docs
- **Console H2:** http://localhost:8080/h2-console

Credenciais do console H2:

| Campo     | Valor                  |
|-----------|------------------------|
| JDBC URL  | `jdbc:h2:mem:taskdb`   |
| Usuário   | `sa`                   |
| Senha     | _(em branco)_          |

## Testes

Execute a suíte de testes com:

```bash
./mvnw test
```

## Estrutura do projeto

```
src/main/java/com/riverfount/taskmanager
├── TaskmanagerApplication.java      # Classe principal
├── config/                          # Configuração do OpenAPI/Swagger
├── controller/                      # Endpoints REST
├── dto/                             # Objetos de requisição/resposta
├── exception/                       # Tratamento global de exceções
├── mapper/                          # Conversão entre Model e DTOs
├── model/                           # Entidades JPA e enums
├── repository/                      # Repositórios Spring Data JPA
└── service/                         # Regras de negócio
```

## Como contribuir

Contribuições são bem-vindas! Siga os passos abaixo:

1. **Faça um fork** do repositório.

2. **Crie uma branch** para sua alteração:

   ```bash
   git switch -c feature/minha-contribuicao
   ```

3. **Implemente** sua alteração seguindo o padrão de código existente e mantendo os testes passando:

   ```bash
   ./mvnw test
   ```

4. **Faça o commit** das suas mudanças com mensagens claras e descritivas:

   ```bash
   git commit -m "feat: descrição objetiva da alteração"
   ```

5. **Envie** a branch para o seu fork:

   ```bash
   git push -u origin feature/minha-contribuicao
   ```

6. **Abra um Pull Request** descrevendo o que foi alterado e o motivo.

### Boas práticas

- Mantenha cada Pull Request focado em uma única alteração.
- Adicione ou atualize testes para novas funcionalidades.
- Siga o padrão de mensagens de commit [Conventional Commits](https://www.conventionalcommits.org/) (ex.: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`).
- Garanta que o build (`./mvnw clean package`) esteja passando antes de abrir o PR.
