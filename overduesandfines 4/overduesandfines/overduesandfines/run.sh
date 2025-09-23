#!/bin/bash

echo "Starting Overdues and Fines Microservice..."
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 21 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
echo "Java version: $JAVA_VERSION"

# Make mvnw executable
chmod +x ./mvnw

# Run the application
echo
echo "Starting application on port 8083..."
echo "Access the application at: http://localhost:8083"
echo "H2 Console at: http://localhost:8083/h2-console"
echo

./mvnw spring-boot:run