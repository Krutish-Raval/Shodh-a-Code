# Contest API & Live Judge System

A complete Spring Boot application with MongoDB that provides a REST API for coding contests and a Docker-based live code judging engine.

## Features

- **REST API** for contest management
- **Live Code Judge** using Docker containers
- **MongoDB** for data persistence
- **Asynchronous Processing** for submissions
- **Multi-language Support** (Java, Python, C++, C)
- **Resource Limits** (time and memory)
- **Live Leaderboard**

## Prerequisites

1. **Java 17** or higher
2. **MongoDB Atlas** cloud database (already configured)
3. **Docker** installed and running
4. **Maven** (or use the included Maven wrapper)

## Setup Instructions

### 1. MongoDB Atlas Configuration

The application is already configured to use MongoDB Atlas cloud database:

- **Database**: `shodh_contest_db`
- **Cluster**: `cluster0.s9ri8.mongodb.net`
- **Connection**: Secure SSL connection with authentication

### 2. Build Docker Image

Build the custom Docker image for code execution:

```bash
docker build -t code-judge .
```

### 3. Run the Application

```bash
# Using Maven wrapper
mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Contest Endpoints

- `GET /api/contests/{contestId}` - Get contest details
- `GET /api/contests/{contestId}/problems` - Get contest problems
- `GET /api/contests/{contestId}/leaderboard` - Get live leaderboard

### Submission Endpoints

- `POST /api/submissions` - Submit code for judging
- `GET /api/submissions/{submissionId}` - Get submission status/result
- `GET /api/submissions/user/{userId}` - Get user's submissions
- `GET /api/submissions/contest/{contestId}` - Get contest submissions

## Sample Data

The application automatically creates sample data on startup:

- **3 Users**: john_doe, jane_smith, alice_wonder
- **1 Contest**: "Spring Boot Coding Contest 2024"
- **3 Problems**: Two Sum, Palindrome Number, Binary Tree Inorder Traversal

## Testing the API

### 1. Get Contest Details

```bash
curl http://localhost:8080/api/contests/{contestId}
```

### 2. Submit Code

```bash
curl -X POST http://localhost:8080/api/submissions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user_id_here",
    "problemId": "problem_id_here",
    "contestId": "contest_id_here",
    "code": "public class Solution {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}",
    "language": "java"
  }'
```

### 3. Check Submission Status

```bash
curl http://localhost:8080/api/submissions/{submissionId}
```

### 4. Get Leaderboard

```bash
curl http://localhost:8080/api/contests/{contestId}/leaderboard
```

## Supported Languages

- **Java**: Uses OpenJDK 17
- **Python**: Uses Python 3.11
- **C++**: Uses GCC compiler
- **C**: Uses GCC compiler

## Resource Limits

- **Time Limit**: 2-5 seconds (configurable per problem)
- **Memory Limit**: 128-256 MB (configurable per problem)
- **CPU**: 1 core per execution
- **Network**: Disabled for security

## Security Features

- **Sandboxed Execution**: Code runs in isolated Docker containers
- **Resource Limits**: Strict time and memory constraints
- **Read-only Filesystem**: Containers can't modify host files
- **No Network Access**: Containers can't access external networks
- **Non-root User**: Code executes as non-privileged user

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   REST API      │    │   MongoDB       │    │   Docker        │
│   Controllers   │◄──►│   Database      │    │   Containers    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Services      │    │   Repositories   │    │   Code Judge    │
│   Business      │    │   Data Access    │    │   Engine        │
│   Logic         │    │   Layer          │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Configuration

Key configuration properties in `application.properties`:

```properties
# MongoDB Atlas Configuration
spring.data.mongodb.uri=mongodb+srv://Krutish:learning123@cluster0.s9ri8.mongodb.net/shodh_contest_db?retryWrites=true&w=majority

# Docker Configuration
docker.execution.timeout=5000
docker.execution.memory-limit=128m

# Async Configuration
spring.task.execution.pool.core-size=5
spring.task.execution.pool.max-size=10
```

## Troubleshooting

### Common Issues

1. **MongoDB Atlas Connection Error**

   - Ensure your IP address is whitelisted in MongoDB Atlas
   - Check if the database user has proper permissions
   - Verify the connection string is correct

2. **Docker Execution Error**

   - Ensure Docker is running
   - Build the Docker image: `docker build -t code-judge .`
   - Check Docker daemon status

3. **JAVA_HOME Error**
   - Set JAVA_HOME environment variable
   - Ensure Java 17+ is installed

### Logs

Check application logs for detailed error information:

```bash
tail -f logs/application.log
```

## Development

### Project Structure

```
src/main/java/com/shodh/shodh_a_code/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── model/          # MongoDB entities
├── repository/     # Data access layer
└── service/        # Business logic
```

### Adding New Languages

1. Update `DockerExecutionService.java`:

   - Add language to `getFileName()`
   - Add Docker image to `getDockerImage()`
   - Add execution command to `getExecutionCommand()`

2. Update Dockerfile to include new runtime

3. Test with sample submissions

## License

This project is licensed under the MIT License.
