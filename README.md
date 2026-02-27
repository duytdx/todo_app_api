# Todo Application - Spring Boot REST API

## 📋 Project Overview

A secure RESTful API for managing todo tasks with user authentication and role-based access control. Built with Spring Boot and JWT authentication.

---

## 🛠️ Tech Stack

### Backend Framework
- **Spring Boot 4.0.3** - Core framework
- **Java 17** - Programming language
- **Maven** - Build tool & dependency management

### Security & Authentication
- **Spring Security** - Authentication & authorization
- **JWT (JSON Web Tokens)** - Token-based authentication
- **JJWT 0.11.5** - JWT library implementation
- **BCrypt** - Password hashing

### Data & Persistence
- **Spring Data JPA** - ORM layer
- **Hibernate** - JPA implementation
- **MySQL** - Relational database

### Documentation & Validation
- **Springdoc OpenAPI 3.0.1** - API documentation (Swagger UI)
- **Jakarta Validation** - Request validation

---

## 💾 Database Setup

### 1. Install MySQL
Ensure MySQL Server is installed and running on your machine.

### 2. Create Database
```sql
CREATE DATABASE todo_app;
```

### 3. Configure Database Connection
The application uses environment variables for database configuration. No manual table creation needed - Hibernate will auto-generate tables on first run.

**Database Configuration (application.properties):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_app
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 4. Database Schema
The application automatically creates the following tables:
- **users** - User accounts (id, username, email, password, role)
- **task** - Todo tasks (id, title, description, completed, user_id)

---

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL Server
- Git (optional)

### Environment Variables
Set the following environment variables before running:

**Windows (Command Prompt):**
```cmd
set DB_URL=jdbc:mysql://localhost:3306/todo_app
set DB_USERNAME=root
set DB_PASSWORD=your_mysql_password
set JWT_SECRET=your_secret_key_min_32_characters_long
```

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/todo_app"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:JWT_SECRET="your_secret_key_min_32_characters_long"
```

**Linux/Mac:**
```bash
export DB_URL=jdbc:mysql://localhost:3306/todo_app
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export JWT_SECRET=your_secret_key_min_32_characters_long
```

### Run with Maven
```bash
# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

### Run with JAR
```bash
# Build JAR file
mvn clean package

# Run JAR
java -jar target/todo_app-0.0.1-SNAPSHOT.jar
```

### Access Application
- **Application URL:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs

---

## 🔐 Authentication Flow

### Overview
The application uses **JWT (JSON Web Token)** based authentication with role-based access control.

### Authentication Mechanism

```
┌─────────┐                    ┌─────────┐                    ┌──────────┐
│ Client  │                    │  API    │                    │ Database │
└────┬────┘                    └────┬────┘                    └────┬─────┘
     │                              │                              │
     │ 1. POST /api/auth/login      │                              │
     │ {username, password}         │                              │
     ├─────────────────────────────>│                              │
     │                              │                              │
     │                              │ 2. Verify credentials        │
     │                              ├─────────────────────────────>│
     │                              │                              │
     │                              │ 3. Return user data          │
     │                              │<─────────────────────────────┤
     │                              │                              │
     │ 4. Return JWT token          │                              │
     │<─────────────────────────────┤                              │
     │                              │                              │
     │ 5. Request with JWT          │                              │
     │ Authorization: Bearer <token>│                              │
     ├─────────────────────────────>│                              │
     │                              │                              │
     │                              │ 6. Validate token            │
     │                              │                              │
     │ 7. Return protected resource │                              │
     │<─────────────────────────────┤                              │
     │                              │                              │
```

### User Roles
- **USER** - Can manage their own tasks
- **ADMIN** - Can manage all users and tasks

### JWT Token Structure
```json
{
  "sub": "username",
  "userId": 1,
  "role": "USER",
  "iat": 1709000000,
  "exp": 1709003600
}
```

**Token Expiration:** 60 minutes (3600 seconds)

### Authorization Rules
| Endpoint | USER | ADMIN |
|----------|------|-------|
| POST /api/auth/login | ✅ Public | ✅ Public |
| GET /api/task/ | ✅ Own tasks | ✅ All tasks |
| POST /api/task/ | ✅ | ✅ |
| PUT /api/task/{id} | ✅ | ✅ |
| DELETE /api/task/{id} | ✅ | ✅ |
| GET /api/user/ | ❌ | ✅ |
| POST /api/user/ | ❌ | ✅ |
| PUT /api/user/{id} | ❌ | ✅ |
| DELETE /api/user/{id} | ❌ | ✅ |

---

## 📡 API Endpoints & Sample cURL Commands

### Base URL
```
http://localhost:8080
```

---

### 🔑 Authentication APIs

#### 1. Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzA5MDAwMDAwLCJleHAiOjE3MDkwMDM2MDB9.xxx"
}
```

---

### 👤 User Management APIs (ADMIN Only)

#### 2. Create User
**Endpoint:** `POST /api/user/`

**Description:** Create a new user account (Admin only)

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/user/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "{\"username\":\"johndoe\",\"email\":\"john@example.com\",\"password\":\"password123\",\"role\":\"USER\"}"
```

#### 3. Get All Users (Paginated)
**Endpoint:** `GET /api/user/`

**Query Parameters:**
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 10)

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/user/?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 4. Get User by ID
**Endpoint:** `GET /api/user/{id}`

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/user/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 5. Update User
**Endpoint:** `PUT /api/user/{id}`

**Request Body:**
```json
{
  "username": "johndoe_updated",
  "email": "john.updated@example.com",
  "password": "newpassword123",
  "role": "ADMIN"
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/api/user/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "{\"username\":\"johndoe_updated\",\"email\":\"john.updated@example.com\",\"password\":\"newpassword123\",\"role\":\"ADMIN\"}"
```

#### 6. Delete User
**Endpoint:** `DELETE /api/user/{id}`

**cURL Command:**
```bash
curl -X DELETE http://localhost:8080/api/user/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### ✅ Task Management APIs

#### 7. Create Task
**Endpoint:** `POST /api/task/`

**Description:** Create a new task for authenticated user

**Request Body:**
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API documentation",
  "completed": false
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/task/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "{\"title\":\"Complete project documentation\",\"description\":\"Write comprehensive README and API documentation\",\"completed\":false}"
```

#### 8. Get All Tasks (Paginated)
**Endpoint:** `GET /api/task/`

**Description:**
- **USER role:** Returns only tasks created by the authenticated user
- **ADMIN role:** Returns all tasks in the system

**Query Parameters:**
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 10)

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/task/?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 9. Get Task by ID
**Endpoint:** `GET /api/task/{id}`

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 10. Update Task
**Endpoint:** `PUT /api/task/{id}`

**Request Body:**
```json
{
  "title": "Updated task title",
  "description": "Updated task description",
  "completed": true
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/api/task/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "{\"title\":\"Updated task title\",\"description\":\"Updated task description\",\"completed\":true}"
```

#### 11. Delete Task
**Endpoint:** `DELETE /api/task/{id}`

**cURL Command:**
```bash
curl -X DELETE http://localhost:8080/api/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🧪 Testing Workflow

### Complete Test Scenario

```bash
# Step 1: Login as Admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

# Step 2: Save the token from response
# export TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Step 3: Create a new user
curl -X POST http://localhost:8080/api/user/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"test123\",\"role\":\"USER\"}"

# Step 4: Login as new user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"testuser\",\"password\":\"test123\"}"

# Step 5: Save user token
# export USER_TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Step 6: Create a task
curl -X POST http://localhost:8080/api/task/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d "{\"title\":\"My first task\",\"description\":\"This is a test task\",\"completed\":false}"

# Step 7: Get all tasks
curl -X GET http://localhost:8080/api/task/ \
  -H "Authorization: Bearer $USER_TOKEN"

# Step 8: Update task (mark as completed)
curl -X PUT http://localhost:8080/api/task/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d "{\"title\":\"My first task\",\"description\":\"This is a test task\",\"completed\":true}"
```

---

## 📊 Error Responses

### Common Error Codes

#### 401 Unauthorized
```json
{
  "timestamp": "2026-02-26T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

#### 403 Forbidden
```json
{
  "timestamp": "2026-02-26T10:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/user/"
}
```

#### 400 Bad Request
```json
{
  "timestamp": "2026-02-26T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/task/"
}
```

---

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Application name
spring.application.name=todo_app

# Database configuration
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/todo_app}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT configuration
jwt.secret=${JWT_SECRET}
jwt.issuer=todo-app
jwt.access-token-minutes=60

# Data seeding
app.seeding.enabled=true

# Logging
logging.file.name=logs/todo_app.log
```

### Data Seeding
On first startup, the application automatically seeds:
- Default admin user (if enabled in configuration)
- Sample tasks (if enabled in configuration)

To disable seeding: Set `app.seeding.enabled=false`

---

## 📁 Project Structure

```
todo_app/
├── src/
│   ├── main/
│   │   ├── java/com/example/todo_app/
│   │   │   ├── TodoAppApplication.java          # Main application class
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java          # Security & JWT configuration
│   │   │   │   ├── OpenApiConfig.java           # Swagger configuration
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── module/
│   │   │       ├── Task/
│   │   │       │   ├── controller/              # REST endpoints
│   │   │       │   ├── dto/                     # Data Transfer Objects
│   │   │       │   ├── model/                   # Entity classes
│   │   │       │   ├── repository/              # Data access layer
│   │   │       │   ├── service/                 # Business logic
│   │   │       │   └── seeder/                  # Sample data seeding
│   │   │       └── User/
│   │   │           ├── controller/
│   │   │           ├── dto/
│   │   │           ├── model/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           └── seeder/
│   │   └── resources/
│   │       ├── application.properties           # Configuration
│   │       ├── static/                          # Static resources
│   │       └── templates/                       # Template files
│   └── test/                                    # Test classes
├── logs/                                        # Application logs
├── target/                                      # Build output
├── pom.xml                                      # Maven dependencies
└── README.md                                    # This file
```

---

## 🛡️ Security Best Practices

1. **Never commit sensitive data** - Use environment variables for secrets
2. **Strong JWT secret** - Use minimum 32 characters for JWT_SECRET
3. **HTTPS in production** - Always use HTTPS for production deployments
4. **Password policy** - Implement strong password requirements
5. **Token rotation** - Implement refresh token mechanism for long-term sessions
6. **Rate limiting** - Add rate limiting to prevent brute force attacks
7. **Input validation** - All inputs are validated using Jakarta Validation

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - JWT decoder and documentation
- [Swagger UI](http://localhost:8080/swagger-ui.html) - Interactive API documentation (when app is running)

---

## 📝 Notes

- Default data seeding creates sample users and tasks on first run
- All passwords are hashed using BCrypt
- JWT tokens expire after 60 minutes
- Pagination is supported with default page size of 10
- SQL queries are logged when `spring.jpa.show-sql=true`

---

## 🤝 Support

For issues or questions:
1. Check application logs in `logs/todo_app.log`
2. Review Swagger documentation at `/swagger-ui.html`
3. Verify database connection and credentials
4. Ensure JWT_SECRET environment variable is set

---

**Version:** 0.0.1-SNAPSHOT
**Last Updated:** February 26, 2026

https://roadmap.sh/projects/todo-list-api