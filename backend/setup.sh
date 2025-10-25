#!/bin/bash

# Contest API Setup Script
# This script sets up the complete Contest API & Live Judge system

echo "=== Contest API & Live Judge Setup ==="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "✅ Docker is running"

# MongoDB Atlas Configuration
echo "✅ Using MongoDB Atlas Cloud Database"
echo "   Database: shodh_contest_db"
echo "   Cluster: cluster0.s9ri8.mongodb.net"
echo ""

# Build the Docker image for code execution
echo "🔨 Building Docker image for code execution..."
docker build -t code-judge .
if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully"
else
    echo "❌ Failed to build Docker image"
    exit 1
fi

# Check Java version
echo "☕ Checking Java version..."
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$java_version" -ge 17 ]; then
    echo "✅ Java $java_version is installed"
else
    echo "❌ Java 17 or higher is required. Current version: $java_version"
    exit 1
fi

# Check Maven wrapper
if [ -f "mvnw" ]; then
    echo "✅ Maven wrapper found"
else
    echo "❌ Maven wrapper not found. Please ensure you're in the project root directory."
    exit 1
fi

echo ""
echo "=== Setup Complete! ==="
echo ""
echo "Next steps:"
echo "1. MongoDB Atlas is already configured and ready to use"
echo ""
echo "2. Run the application:"
echo "   ./mvnw spring-boot:run"
echo ""
echo "3. Test the API:"
echo "   ./test-api.sh"
echo ""
echo "4. Or use Docker Compose for complete setup:"
echo "   docker-compose up -d"
echo ""
echo "The application will be available at: http://localhost:8080"
echo "API documentation: http://localhost:8080/api"
echo ""
echo "Sample data will be created automatically on first startup:"
echo "- 3 Users: john_doe, jane_smith, alice_wonder"
echo "- 1 Contest: Spring Boot Coding Contest 2024"
echo "- 3 Problems: Two Sum, Palindrome Number, Binary Tree Inorder Traversal"
