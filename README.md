# Shodh-a-Code 🚀

A full-stack competitive programming platform with real-time code execution, contest management, and live leaderboards. Built with Spring Boot, React, MongoDB Atlas, and Docker.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat-square)
![React](https://img.shields.io/badge/React-19.1.1-blue?style=flat-square)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=flat-square)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Setup Instructions](#-setup-instructions)
- [API Documentation](#-api-documentation)
- [Design Choices & Architecture](#-design-choices--architecture)
- [Project Structure](#-project-structure)
- [Usage](#-usage)

---

## ✨ Features

- **Live Code Execution**: Support for Java, Python, C++, and C with Docker-based sandboxed execution
- **Contest Management**: Create and manage coding contests with multiple problems
- **Real-time Leaderboard**: Track participant rankings based on submissions
- **Secure Execution**: Isolated Docker containers with resource limits (time & memory)
- **Asynchronous Processing**: Non-blocking submission processing with Spring's async capabilities
- **MongoDB Atlas Integration**: Cloud-based database with optimized connection pooling
- **Modern UI**: React + TypeScript frontend with Tailwind CSS
- **RESTful API**: Well-structured API endpoints with proper error handling

---

## 🛠 Tech Stack

### Backend

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MongoDB Atlas (Cloud)
- **Build Tool**: Maven
- **Containerization**: Docker

### Frontend

- **Framework**: React 19.1.1
- **Language**: TypeScript
- **Build Tool**: Vite 7.1.7
- **Styling**: Tailwind CSS 4.1.16
- **HTTP Client**: Axios
- **Code Editor**: React Ace Editor
- **Routing**: React Router DOM

---

## 🚀 Setup Instructions

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 17** or higher
- **Node.js 18+** and npm
- **Docker Desktop** (for code execution)
- **Maven** (or use the included Maven wrapper)
- **Git**

### Option 1: Quick Start with Docker Compose (Recommended)

1. **Clone the repository**

   ```bash
   git clone https://github.com/Krutish-Raval/Shodh-a-Code.git
   cd Shodh-a-Code
   ```

2. **Build the code execution Docker image**

   ```bash
   cd backend
   docker build -t code-judge .
   ```

3. **Start the backend with Docker Compose**

   ```bash
   docker-compose up --build
   ```

   The backend API will be available at `http://localhost:8080`

4. **Open a new terminal and start the frontend**

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   The frontend will be available at `http://localhost:5173`

### Option 2: Manual Setup (Local Development)

#### Backend Setup

1. **Navigate to backend directory**

   ```bash
   cd backend
   ```

2. **Configure MongoDB (Optional)**

   The application is pre-configured with MongoDB Atlas. To use your own database, update `src/main/resources/application.properties`:

   ```properties
   spring.data.mongodb.uri=your-mongodb-connection-string
   ```

3. **Build the code execution Docker image**

   ```bash
   docker build -t code-judge .
   ```

4. **Run the Spring Boot application**

   ```bash
   # Using Maven wrapper (Windows)
   mvnw.cmd spring-boot:run

   # Using Maven wrapper (Linux/Mac)
   ./mvnw spring-boot:run

   # Or using Maven directly
   mvn spring-boot:run
   ```

   The API will start on `http://localhost:8080`

#### Frontend Setup

1. **Navigate to frontend directory**

   ```bash
   cd frontend
   ```

2. **Install dependencies**

   ```bash
   npm install
   ```

3. **Start the development server**

   ```bash
   npm run dev
   ```

   The application will open at `http://localhost:5173`

### Execution Mode Configuration

The application supports two execution modes:

- **Docker Mode** (Production): Executes code in isolated Docker containers

  ```properties
  execution.mode=docker
  ```

- **Local Mode** (Development): Executes code directly on the host machine
  ```properties
  execution.mode=local
  ```

Update `backend/src/main/resources/application.properties` to switch modes.

---

## 📡 API Documentation

### Base URL

```
http://localhost:8080/api
```

### Endpoints

#### Contest Endpoints

| Method | Endpoint                            | Description                   | Response                 |
| ------ | ----------------------------------- | ----------------------------- | ------------------------ |
| `GET`  | `/contests/{contestId}`             | Get contest details           | `ContestDto`             |
| `GET`  | `/contests/{contestId}/problems`    | Get all problems in a contest | `List<ProblemDto>`       |
| `GET`  | `/contests/{contestId}/leaderboard` | Get contest leaderboard       | `List<LeaderboardEntry>` |

#### Submission Endpoints

| Method | Endpoint                           | Description                | Response                   |
| ------ | ---------------------------------- | -------------------------- | -------------------------- |
| `POST` | `/submissions`                     | Submit code for evaluation | `{ submissionId: string }` |
| `GET`  | `/submissions/{submissionId}`      | Get submission details     | `SubmissionDto`            |
| `GET`  | `/submissions/user/{userId}`       | Get user's submissions     | `List<SubmissionDto>`      |
| `GET`  | `/submissions/contest/{contestId}` | Get contest submissions    | `List<SubmissionDto>`      |

#### Debug Endpoint

| Method | Endpoint                               | Description                  | Response         |
| ------ | -------------------------------------- | ---------------------------- | ---------------- |
| `GET`  | `/debug/problem/{problemId}/testcases` | Get test cases for debugging | `List<TestCase>` |

### Request/Response Formats

#### Submit Code (POST /submissions)

**Request Body:**

```json
{
  "userId": "user123",
  "problemId": "problem456",
  "contestId": "contest789",
  "language": "JAVA",
  "code": "public class Solution { ... }"
}
```

**Response:**

```json
{
  "submissionId": "submission-uuid"
}
```

#### Get Submission (GET /submissions/{submissionId})

**Response:**

```json
{
  "id": "submission-uuid",
  "userId": "user123",
  "problemId": "problem456",
  "language": "JAVA",
  "code": "public class Solution { ... }",
  "status": "ACCEPTED",
  "executionTime": 150,
  "memoryUsed": 2048,
  "submittedAt": "2025-10-26T10:30:00Z",
  "testCaseResults": [
    {
      "passed": true,
      "executionTime": 50,
      "output": "Expected output",
      "error": null
    }
  ]
}
```

#### Get Leaderboard (GET /contests/{contestId}/leaderboard)

**Response:**

```json
[
  {
    "userId": "user123",
    "userName": "Alice",
    "score": 300,
    "solvedProblems": 3,
    "totalSubmissions": 5,
    "lastSubmissionTime": "2025-10-26T10:30:00Z"
  }
]
```

### Status Codes

| Status                      | Description                   |
| --------------------------- | ----------------------------- |
| `200 OK`                    | Request successful            |
| `201 CREATED`               | Resource created successfully |
| `400 BAD REQUEST`           | Invalid request data          |
| `404 NOT FOUND`             | Resource not found            |
| `500 INTERNAL SERVER ERROR` | Server error                  |

### Submission Status Values

- `PENDING`: Submission queued for processing
- `RUNNING`: Code is currently being executed
- `ACCEPTED`: All test cases passed
- `WRONG_ANSWER`: Output doesn't match expected result
- `TIME_LIMIT_EXCEEDED`: Execution took too long
- `RUNTIME_ERROR`: Code crashed during execution
- `COMPILATION_ERROR`: Code failed to compile

---

## 🏗 Design Choices & Architecture

### Backend Architecture

#### Service Layer Structure

The backend follows a **layered architecture** with clear separation of concerns:

1. **Controller Layer** (`controller/`)

   - Handles HTTP requests and responses
   - Routes to appropriate service methods
   - Implements CORS for frontend communication
   - Minimal business logic (focused on HTTP concerns)

2. **Service Layer** (`service/`)

   - **ContestService**: Manages contest CRUD operations and leaderboard calculation
   - **SubmissionService**: Handles submission lifecycle and validation
   - **SubmissionProcessingService**: Orchestrates asynchronous code execution
   - **DockerExecutionService**: Manages Docker-based code execution
   - **LocalExecutionService**: Provides local execution for development
   - **DataInitializationService**: Seeds initial contest and problem data

3. **Repository Layer** (`repository/`)

   - Spring Data MongoDB repositories
   - Abstract database operations
   - Custom query methods for complex lookups

4. **Model/DTO Layer** (`model/`, `dto/`)
   - **Models**: MongoDB document entities
   - **DTOs**: Data transfer objects for API communication
   - Clear separation between persistence and presentation

#### Key Design Decisions

**1. Async Processing with @Async**

```java
@Async
public CompletableFuture<Void> processSubmissionAsync(String submissionId)
```

- **Why**: Code execution can take several seconds. Async processing prevents blocking the HTTP thread
- **Trade-off**: Added complexity in error handling and status tracking
- **Benefit**: Better scalability and user experience (instant submission acceptance)

**2. Strategy Pattern for Execution Modes**

Two execution strategies based on environment:

- **DockerExecutionService**: Production-grade isolated execution
- **LocalExecutionService**: Fast development without Docker overhead

**Why**: Development doesn't always require Docker security overhead
**Configuration**: `execution.mode` property switches between strategies

**3. MongoDB Atlas Cloud Database**

- **Why**: Simplified deployment, no local database management
- **Benefit**: Built-in replication, backups, and scaling
- **Trade-off**: Network latency (mitigated with connection pooling)
- **Optimization**:
  ```properties
  max-connection-pool-size=50
  min-connection-pool-size=5
  ```

**4. Resource Limiting**

- Time limits per test case (configurable per problem)
- Memory limits enforced by Docker (or process monitoring in local mode)
- Container isolation prevents resource exhaustion

#### Challenges & Solutions

**Challenge 1: Docker Container Management**

- **Problem**: Spawning containers per submission could lead to resource leaks
- **Solution**:
  - Implemented proper container cleanup in finally blocks
  - Used ephemeral containers with `--rm` flag
  - Timeout enforcement to kill hanging containers

**Challenge 2: Test Case Output Comparison**

- **Problem**: Different newline formats and trailing spaces
- **Solution**: Normalized output comparison (trim, normalize line endings)

**Challenge 3: Compilation vs Runtime Errors**

- **Problem**: Distinguishing between compilation failures and runtime crashes
- **Solution**:
  - Separate compilation step with exit code checking
  - Parse compiler error messages
  - Different status codes for each failure type

### Frontend Architecture

#### State Management Approach

**React Component State + Context**

- **Why**: Application state is relatively simple (contest data, submission status)
- **No Redux/Zustand**: Avoided over-engineering for this scale
- **Trade-off**: Less centralized state but simpler codebase
- **Approach**:
  - Local state in components (`useState`)
  - Props drilling for shared data
  - API service layer for data fetching

#### Component Structure

```
components/
├── JoinPage.tsx          // Landing page with contest ID input
├── ContestPage.tsx       // Main contest view with problem list
├── ProblemView.tsx       // Problem details and code submission
├── CodeEditor.tsx        // Monaco-based code editor component
├── SimpleCodeEditor.tsx  // Lightweight textarea fallback
└── Leaderboard.tsx       // Real-time rankings display
```

**Design Decision**: Component composition over complex state management

- Each component manages its own data fetching
- Polling for submission status updates (simple but effective)
- Direct API calls via Axios service

#### Key Design Decisions

**1. React Router for Navigation**

- Single-page application with client-side routing
- Contest ID passed via URL params or state
- Clean URL structure: `/` → `/contest?id=xxx`

**2. Tailwind CSS for Styling**

- **Why**: Rapid UI development with utility-first approach
- **Benefit**: Consistent design system without custom CSS
- **Trade-off**: Larger HTML class attributes (mitigated by build optimization)

**3. Axios Service Layer**

```typescript
// Centralized API configuration
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: { "Content-Type": "application/json" },
});
```

- Single source of truth for API base URL
- Interceptors can be added for auth/logging
- Type-safe with TypeScript interfaces

**4. Polling for Submission Updates**

- **Why**: WebSocket complexity not justified for this scale
- **Implementation**: `setInterval` to check submission status
- **Trade-off**: More API calls but simpler implementation
- **Future**: Could migrate to WebSocket for true real-time updates

#### Challenges & Solutions

**Challenge 1: Code Editor Integration**

- **Problem**: Monaco Editor bundle size too large
- **Solution**:
  - Primary: React Ace Editor (lighter weight)
  - Fallback: Simple textarea for unsupported environments

**Challenge 2: Real-time Leaderboard**

- **Problem**: Stale data without real-time updates
- **Solution**: Auto-refresh on submission completion + manual refresh button

**Challenge 3: Error Handling**

- **Problem**: Network failures and API errors
- **Solution**:
  - Try-catch blocks with user-friendly error messages
  - Loading states to prevent double submissions
  - Toast notifications for feedback

### Docker Orchestration

#### Architecture

**Multi-Container Setup:**

```yaml
services:
  app: # Spring Boot application
  code-judge: # Execution container (dynamically spawned)
```

#### Design Decisions

**1. Docker-in-Docker (DinD) Approach**

```yaml
volumes:
  - /var/run/docker.sock:/var/run/docker.sock
```

- **Why**: Spring Boot app spawns execution containers on-demand
- **Benefit**: Isolated execution per submission
- **Trade-off**: Requires Docker socket access (privileged mode)
- **Security**: Production would use Kubernetes Jobs or separate execution nodes

**2. Custom Base Image for Code Execution**

```dockerfile
FROM ubuntu:22.04
RUN apt-get install openjdk-17-jdk python3 gcc g++
```

- **Why**: Single image supports multiple languages
- **Trade-off**: Larger image size vs. language-specific images
- **Benefit**: Simpler orchestration, faster switching between languages

**3. Ephemeral Containers**

- Containers are created, used, and destroyed per submission
- `--rm` flag ensures automatic cleanup
- No persistent storage (code injected at runtime)

#### Challenges & Trade-offs

**Challenge 1: Container Startup Overhead**

- **Problem**: Each container takes ~1-2 seconds to start
- **Mitigation**:
  - Pre-pulling the `code-judge` image
  - Future: Container pooling for frequently used languages

**Challenge 2: Platform Independence**

- **Problem**: Docker socket path differs (Windows vs Linux)
- **Solution**:
  - Docker Desktop for Windows handles path mapping
  - Documentation includes platform-specific instructions

**Challenge 3: Resource Limits**

- **Problem**: Preventing resource exhaustion attacks
- **Implementation**:
  - Docker `--memory` and `--cpus` flags
  - Application-level timeouts
  - Process monitoring and forced termination

**Trade-off**: Docker adds complexity but provides essential security and isolation for untrusted code execution

---

## 📂 Project Structure

```
Shodh-a-Code/
├── backend/                        # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/shodh/shodh_a_code/
│   │   │   │   ├── controller/     # REST API controllers
│   │   │   │   ├── service/        # Business logic services
│   │   │   │   ├── model/          # MongoDB entities
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   ├── repository/     # MongoDB repositories
│   │   │   │   ├── config/         # Configuration classes
│   │   │   │   └── util/           # Utility classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                   # Unit and integration tests
│   ├── Dockerfile                  # Code execution container image
│   ├── docker-compose.yml          # Docker orchestration
│   └── pom.xml                     # Maven dependencies
│
├── frontend/                       # React frontend
│   ├── src/
│   │   ├── components/             # React components
│   │   │   ├── JoinPage.tsx
│   │   │   ├── ContestPage.tsx
│   │   │   ├── ProblemView.tsx
│   │   │   ├── CodeEditor.tsx
│   │   │   └── Leaderboard.tsx
│   │   ├── services/
│   │   │   └── api.ts              # Axios API client
│   │   ├── App.tsx                 # Main app component
│   │   └── main.tsx                # Entry point
│   ├── package.json                # npm dependencies
│   ├── vite.config.ts              # Vite configuration
│   └── tailwind.config.js          # Tailwind CSS config
│
└── README.md                       # This file
```

---

## Usage

### Quick Start Demo

To quickly test the application, use these credentials:

- **Contest ID**: `68fd1377ad086d9b118e8d65`
- **Username**: `john_doe` (or any username of your choice)

### For Participants

1. **Join a Contest**

   - Navigate to `http://localhost:5173`
   - Enter contest ID: `68fd1377ad086d9b118e8d65`
   - Enter your username (e.g., `john_doe`)
   - Click "Join Contest"

2. **Solve Problems**

   - View problem list and select a problem
   - Read problem description and constraints
   - Write code in the integrated editor
   - Select programming language (Java, Python, C++, C)

3. **Submit Solution**

   - Click "Submit" to evaluate your code
   - View test case results in real-time
   - Check submission status (Accepted, Wrong Answer, etc.)

4. **Track Progress**
   - View your submissions in the history
   - Check leaderboard for rankings
   - Compete with other participants

### For Organizers/Developers

1. **Initialize Contest Data**

   - The `DataInitializationService` seeds initial data on startup
   - Modify `DataInitializationService.java` to add contests/problems

2. **Monitor Submissions**

   - Backend logs show execution details
   - Debug endpoint: `/debug/problem/{problemId}/testcases`

3. **Configure Execution Mode**
   - Edit `application.properties`: `execution.mode=docker` or `local`
   - Docker mode for production, local for development

---

## 🔒 Security Considerations

- **Sandboxed Execution**: Docker containers isolate untrusted code
- **Resource Limits**: Time and memory constraints prevent abuse
- **Non-root User**: Code executes as `codeuser` (not root)
- **No Network Access**: Execution containers have no internet connectivity
- **Input Validation**: Server-side validation of all API inputs

---

## 🚧 Future Enhancements

- [ ] WebSocket support for real-time updates
- [ ] User authentication and authorization
- [ ] Contest creation UI (admin panel)
- [ ] Enhanced security with Kubernetes Jobs
- [ ] Code plagiarism detection
- [ ] Detailed execution analytics
- [ ] Mobile-responsive design improvements
- [ ] Support for more programming languages
- [ ] Contest scheduling and notifications

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open-source and available under the MIT License.

---

## 👨‍💻 Author

**Krutish Raval**

- GitHub: [@Krutish-Raval](https://github.com/Krutish-Raval)

---

## 🙏 Acknowledgments

- Spring Boot for the robust backend framework
- React and Vite for the modern frontend tooling
- MongoDB Atlas for cloud database services
- Docker for containerization technology
- Tailwind CSS for beautiful UI components

---

**Happy Coding! 🚀**
