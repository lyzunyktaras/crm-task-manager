# CRM Application

A CRM application for managing clients, tasks, and contacts, with support for analytics and WebSocket notifications.

---
## Run via docker compose
```bash
  docker-compose up --build
```
### or 
```bash
  docker compose up --build
```
---
## ▼ Or follow next steps

## Requirements

Ensure the following are installed before proceeding:
- **Java 21** or higher
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Redis** (for caching)
- **Node.js 16+** and **npm**

---

## Installation and Setup

### 1. Clone the Repository
```bash
    git clone https://github.com/lyzunyktaras/crm.git
```
### 2. Ensure PostgreSQL and Redis are running:
1. Create a database named `crm` in PostgreSQL:
```sql
CREATE DATABASE crm;
```
2. **Run the DDL script**:
Navigate to the root path of the project and execute the `init_h2.sql` file.
This will create all necessary database tables and relationships.

3. Load sample data: After running the DDL script, execute the `dump.sql` file to populate the database with sample data for testing and development.

### 3. Running the Backend
Use Maven to build and run the backend:
```bash
  mvn clean install
  mvn spring-boot:run
```

### 4. Frontend Configuration
Navigate to the `webapp` folder:
```bash
  cd webapp
```

Install dependencies:
```bash
  npm install
```

Run the development server:
```bash
  npm start
```

---

## API Endpoints

### Authentication
Include the JWT token in the `Authorization` header for secured endpoints:
```
Authorization: Bearer <token>
```
---
### Default user
- **username**: admin
- **passowrd**: admin
---

### Example Endpoints:
- **Client List**: `GET /api/client`
- **Task List**: `GET /api/task`

---

### Example API Endpoints:
- **Client Statistics**: `GET /api/v1/statistics/clients`
- **Task Statistics**: `GET /api/v1/statistics/tasks`

---

## Running Tests

### Backend Tests:
Run unit and integration tests:
```bash
  mvn test
```
---
