# Multi-language Docker image for code execution
FROM ubuntu:22.04

# Install system dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    python3 \
    python3-pip \
    gcc \
    g++ \
    make \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Create a non-root user for security
RUN useradd -m -s /bin/bash codeuser && \
    chown -R codeuser:codeuser /app

# Switch to non-root user
USER codeuser

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Default command
CMD ["/bin/bash"]
