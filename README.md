# 📝 Task Management Microservice

A small Task Management microservice that demonstrates your ability to design a layered architecture and containerize with Docker
---

## 🚀 Key Features

- **CRUD Operations for Tasks**
- **Filters by due date, priority, and completion status**
- **Database: MySQL/PostgreSQL**
- **Microservice built with Java 8+ and Spring Boot**
- **Docker containerization**
- **Overdue Task Notifier (AWS Lambda)**

---

## 🛠️ Project Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/rebolloomare/task-management-service.git
cd task-management-service
```

### 2️⃣ Configure the Database
📦 The microservice uses PostgreSQL

Ensure the connection variables in application.properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/assessment
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3️⃣ Create the Table
Run the corresponding SQL script:

./db/scripts/postgres_tasks.sql

⚙️ Build & Run Steps

🏗️ Build the Project
```bash
./gradlew clean build
```

▶️ Run Locally
```bash
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar
```

🐳 Run with Docker
```bash
docker build -t taskmanager:latest .
docker run -p 8080:8080 taskmanager:latest
```

📬 Example Requests

🔹 Create Task
```http request
POST /api/v1/tasks
Content-Type: application/json
{
"title": "Aprender Spring Boot",
"description": "Revisar la documentación oficial",
"dueDate": "2025-06-10",
"priority": "HIGH"
}
```

🔹 List Tasks with Filters
```http request
GET /api/v1/tasks?dueBefore=2025-06-15&completed=false
```

🔹 Get Task by ID
```http request
GET /api/v1/tasks/{id}
```

🔹 Update Task
```http request
PUT /api/v1/tasks/{id}
Content-Type: application/json

{
"title": "Aprender Spring Boot (actualizado)",
"completed": true
}
```

🔹 Delete Task
```http request
DELETE /api/v1/tasks/{id}
```

⏱️ Time Spent

| Activity                            | Estimated Time 🕒 |
| ----------------------------------- |-------------------|
| Domain Modeling & SQL               | 30 min            |
| Layered Architecture Implementation | 1 h               |
| REST Endpoints                      | 1 h               |
| Dockerfile & Containerization       | 30 min            |
| Lambda for Overdue Tasks            | 1 h               |
| README & Documentation              | 30 min            |
| **Total Approximate**               | **4 h**           |

📦 Docker Hub and deployment
👉 El contenedor está listo para publicar:
```bash
docker tag taskmanager:latest yourdockerhubusername/taskmanager:latest
docker push yourdockerhubusername/taskmanager:latest
```

📧 Contact

If you have any questions or suggestions, feel free to email me at omar.rebollo@gmail.com
