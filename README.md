# 📋 Crud-API

A RESTful CRUD API built with **Spring Boot 4**, **PostgreSQL**, and **JWT authentication**. Supports full task management with user registration, login, and role-based access control. Ready to run with Docker Compose.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT (jjwt 0.13) |
| Validation | Jakarta Validation |
| Documentation | Swagger / OpenAPI 3 (springdoc) |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |
| Utilities | Lombok |

---

## 📁 Project Structure

```
crud-api/
├── src/
│   └── main/
│       ├── java/com/chema/db/crudapi/
│       │   ├── auth/                  # Registration & login
│       │   │   ├── AuthController.java
│       │   │   ├── AuthService.java
│       │   │   ├── AuthRequest.java
│       │   │   └── AuthResponse.java
│       │   ├── controller/            # Task endpoints
│       │   │   └── TaskController.java
│       │   ├── service/               # Business logic
│       │   │   ├── TaskService.java
│       │   │   └── TaskMapper.java
│       │   ├── model/                 # JPA entities & enums
│       │   │   ├── Task.java
│       │   │   ├── User.java
│       │   │   ├── Role.java
│       │   │   └── TaskStatus.java
│       │   ├── dto/                   # Request / Response objects
│       │   │   ├── TaskRequest.java
│       │   │   └── TaskResponse.java
│       │   ├── repository/            # Spring Data repositories
│       │   │   ├── TaskRepository.java
│       │   │   └── UserRepository.java
│       │   ├── security/              # JWT filter & configuration
│       │   │   ├── SecurityConfig.java
│       │   │   ├── JwtService.java
│       │   │   └── JwtAuthenticationFilter.java
│       │   └── exception/             # Global error handling
│       │       ├── GlobalExceptionHandler.java
│       │       └── TaskNotFoundException.java
│       └── resources/
│           └── application.properties
├── Dockerfile
└── docker-compose.yml
```

---

## ⚙️ Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose installed
- Java 17+ (only needed if running without Docker)
- Maven (only needed if running without Docker)

### Run with Docker Compose (recommended)

```bash
# 1. Clone the repository
git clone https://github.com/JoseGodoy96/Crud-API.git
cd Crud-API/crud-api

# 2. Build the JAR
./mvnw clean package -DskipTests

# 3. Start all services (PostgreSQL + Spring Boot)
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### Run locally (without Docker)

```bash
# Make sure PostgreSQL is running on localhost:5432
# with database: crudapi, user: postgres, password: postgres

./mvnw spring-boot:run
```

---

## 🔐 Authentication

The API uses **JWT Bearer tokens**. All `/api/tasks/**` endpoints require authentication.

### Register a new user

```http
POST /auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response `201 Created`:**
```json
{
  "message": "User registered successfully"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use the token in subsequent requests:
```
Authorization: Bearer <token>
```

---

## 📌 API Endpoints

All task endpoints require a valid JWT token.

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/tasks` | Get all tasks |
| `GET` | `/api/tasks/{id}` | Get task by ID |
| `POST` | `/api/tasks` | Create a new task |
| `PUT` | `/api/tasks/{id}` | Update an existing task |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

### Request body — Create / Update task

```json
{
  "title": "My first task",
  "description": "Optional description",
  "status": "PENDING"
}
```

| Field | Type | Required | Constraints |
|---|---|---|---|
| `title` | String | ✅ Yes | Max 100 characters |
| `description` | String | ❌ No | Max 255 characters |
| `status` | Enum | ❌ No | `PENDING`, `IN_PROGRESS`, `COMPLETED` |

### Response — Task

```json
{
  "id": 1,
  "title": "My first task",
  "description": "Optional description",
  "status": "PENDING",
  "createdAt": "2025-06-14T10:00:00",
  "updatedAt": "2025-06-14T10:00:00"
}
```

---

## 📖 Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

No authentication required to access the docs.

---

## 🛡️ Security

- Passwords are hashed with **BCrypt** before being stored.
- JWT tokens expire after **1 hour**.
- Session management is **stateless** — no server-side sessions.
- Role-based access: registered users receive the `USER` role by default.

---

## 🧪 Tests

The project includes unit tests for the main layers:

```bash
./mvnw test
```

| Test class | Coverage |
|---|---|
| `AuthServiceTest` | Registration and login logic |
| `TaskServiceTest` | CRUD business logic |
| `TaskControllerTest` | REST endpoints (MockMvc) |

---

## 🗃️ Data Model

### Task

| Column | Type | Notes |
|---|---|---|
| `id` | Long | Auto-generated primary key |
| `title` | String | Required, max 100 chars |
| `description` | String | Optional, max 255 chars |
| `status` | Enum | `PENDING` by default |
| `createdAt` | LocalDateTime | Set automatically on create |
| `updatedAt` | LocalDateTime | Updated automatically on save |

### User

| Column | Type | Notes |
|---|---|---|
| `id` | Long | Auto-generated primary key |
| `username` | String | Unique, 3–30 chars |
| `password` | String | BCrypt hashed |
| `role` | Enum | `USER` or `ADMIN` |

---

## 🐳 Docker

The `docker-compose.yml` starts two services:

| Service | Port | Description |
|---|---|---|
| `postgres` | `5434:5432` | PostgreSQL 16 database |
| `app` | `8080:8080` | Spring Boot application |

PostgreSQL data is persisted in a named Docker volume (`postgres_data`).

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 👤 Author

**Jose Godoy**  
[github.com/JoseGodoy96](https://github.com/JoseGodoy96)

