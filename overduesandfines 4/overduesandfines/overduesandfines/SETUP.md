# Overdues and Fines Microservice - Setup Guide

## Prerequisites

### Required Software
1. **Java 21** (JDK 21 or higher)
   - Download from: https://adoptium.net/
   - Verify: `java -version`

2. **Maven 3.6+** (or use included wrapper)
   - Download from: https://maven.apache.org/
   - Verify: `mvn -version`

3. **IDE** (Optional but recommended)
   - IntelliJ IDEA, Eclipse, or VS Code

## Quick Start

### Option 1: Using Maven Wrapper (Recommended)
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Option 2: Using Local Maven
```bash
mvn spring-boot:run
```

### Option 3: Using JAR
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/overduesandfines-0.0.1-SNAPSHOT.jar
```

## Application URLs
- **Main Application**: http://localhost:8083
- **H2 Database Console**: http://localhost:8083/h2-console
- **Health Check**: http://localhost:8083/actuator/health

## Database Configuration
- **Type**: H2 (In-memory)
- **URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: password

## Authentication
- **Username**: user
- **Password**: Check console output for generated password

## Troubleshooting

### Common Issues

1. **Port 8083 already in use**
   - Change port in `application.yml`: `server.port: 8084`

2. **Java version mismatch**
   - Ensure Java 21+ is installed and set as JAVA_HOME

3. **Maven dependencies not downloading**
   - Run: `mvn clean install -U`

4. **Feign client connection errors**
   - Normal if Member/Book services are not running
   - Service will still start successfully

## Testing the Application

### Using cURL
```bash
# Get all overdues
curl -u user:password http://localhost:8083/api/overdues

# Create overdue
curl -u user:password -X POST http://localhost:8083/api/overdues \
  -H "Content-Type: application/json" \
  -d '{"member":{"id":1,"name":"John","memberId":"M001"},"book":{"id":1,"title":"Java Book","author":"Author"},"dueDate":"2024-01-15","resolved":false}'
```

### Using Postman
Import the collection from the main README or use the URLs provided in the documentation.

## Environment Variables (Optional)
```bash
# Custom port
SERVER_PORT=8084

# Custom database
SPRING_DATASOURCE_URL=jdbc:h2:mem:customdb

# External service URLs
MEMBER_SERVICE_URL=http://localhost:8081
BOOK_SERVICE_URL=http://localhost:8082
```

## Building for Production
```bash
# Create executable JAR
mvn clean package -DskipTests

# The JAR will be in target/ directory
# Copy and run on any system with Java 21+
```