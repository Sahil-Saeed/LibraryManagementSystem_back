@echo off
echo Starting Overdues and Fines Microservice...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 or higher
    pause
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
echo Java version: %JAVA_VERSION%

REM Run the application
echo.
echo Starting application on port 8083...
echo Access the application at: http://localhost:8083
echo H2 Console at: http://localhost:8083/h2-console
echo.

mvnw.cmd spring-boot:run

pause