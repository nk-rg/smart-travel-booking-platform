@echo off

echo Building Smart Travel Booking Platform (Parent Project)...
echo =========================================================

REM Build all modules from parent project
echo Building all microservices from parent POM...
call mvn clean package -DskipTests

if %errorlevel% equ 0 (
    echo ✅ All services built successfully!
    echo.
    echo 📋 Built modules:
    echo • api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
    echo • travel-package-service/target/travel-package-service-0.0.1-SNAPSHOT.jar
    echo • booking-service/target/booking-service-0.0.1-SNAPSHOT.jar
    echo • notification-service/target/notification-service-0.0.1-SNAPSHOT.jar
    echo.
    echo 🚀 Run 'docker-compose up --build' to start the platform
) else (
    echo ❌ Build failed!
    pause
    exit /b 1
)

pause
