@echo off

echo ===================================
echo Smart Travel Booking Platform Setup
echo ===================================

REM Check prerequisites
echo Checking prerequisites...

REM Check Java
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Java found
) else (
    echo ❌ Java 17+ required but not found
    pause
    exit /b 1
)

REM Check Maven
mvn -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Maven found
) else (
    echo ❌ Maven required but not found
    pause
    exit /b 1
)

REM Check Docker
docker --version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Docker found
) else (
    echo ❌ Docker required but not found
    pause
    exit /b 1
)

echo.
echo Building all microservices from parent POM...
echo ==============================================

REM Build all services using parent POM
call mvn clean package -DskipTests

if %errorlevel% equ 0 (
    echo ✅ All services built successfully from parent project!
) else (
    echo ❌ Failed to build services
    pause
    exit /b 1
)

echo.
echo Starting Docker containers...
echo ============================

docker-compose up -d

echo.
echo Waiting for services to start...
timeout /t 30 /nobreak >nul

echo.
echo 🚀 Smart Travel Booking Platform is ready!
echo ===========================================
echo.
echo 📋 Available Services:
echo • API Gateway: http://localhost:8080
echo • Travel Packages: http://localhost:8081/api/travel-packages
echo • Bookings: http://localhost:8082/api/bookings
echo • Notifications: http://localhost:8083/api/notifications/health
echo.
echo 📊 Sample API calls:
echo • Get all packages: curl http://localhost:8080/api/travel-packages
echo • Create booking: curl -X POST http://localhost:8080/api/bookings -H "Content-Type: application/json" -d "{\"userId\":\"user123\",\"packageId\":\"1\",\"quantity\":2,\"packagePrice\":1299.99}"
echo.
echo 📖 Import 'STBP-API-Collection.postman_collection.json' in Postman for complete API testing
echo.
echo 🔧 Development commands:
echo • Build all: mvn clean package
echo • Run specific service: mvn spring-boot:run -pl api-gateway
echo • Clean all: mvn clean
echo.

pause
