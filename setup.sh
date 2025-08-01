#!/bin/bash

echo "==================================="
echo "Smart Travel Booking Platform Setup"
echo "==================================="

# Check prerequisites
echo "Checking prerequisites..."

# Check Java
java_version=$(java -version 2>&1 | grep -oP '(?<=java version ")[^"]*')
if [ $? -eq 0 ]; then
    echo "✅ Java found: $java_version"
else
    echo "❌ Java 17+ required but not found"
    exit 1
fi

# Check Maven
mvn_version=$(mvn -version 2>&1 | grep -oP '(?<=Apache Maven )[^\s]*')
if [ $? -eq 0 ]; then
    echo "✅ Maven found: $mvn_version"
else
    echo "❌ Maven required but not found"
    exit 1
fi

# Check Docker
docker_version=$(docker --version 2>&1 | grep -oP '(?<=Docker version )[^\s,]*')
if [ $? -eq 0 ]; then
    echo "✅ Docker found: $docker_version"
else
    echo "❌ Docker required but not found"
    exit 1
fi

echo ""
echo "Building all microservices from parent POM..."
echo "=============================================="

# Build all services using parent POM
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ All services built successfully from parent project!"
else
    echo "❌ Failed to build services"
    exit 1
fi

echo ""
echo "Starting Docker containers..."
echo "============================"

docker-compose up -d

echo ""
echo "Waiting for services to start..."
sleep 30

echo ""
echo "Service Status Check:"
echo "===================="

# Check each service
services_urls=(
    "http://localhost:8080/actuator/health|API Gateway"
    "http://localhost:8081/actuator/health|Travel Package Service" 
    "http://localhost:8082/actuator/health|Booking Service"
    "http://localhost:8083/api/notifications/health|Notification Service"
)

for service_url in "${services_urls[@]}"; do
    IFS='|' read -r url name <<< "$service_url"
    
    if curl -s "$url" > /dev/null 2>&1; then
        echo "✅ $name is running"
    else
        echo "❌ $name is not responding"
    fi
done

echo ""
echo "🚀 Smart Travel Booking Platform is ready!"
echo "==========================================="
echo ""
echo "📋 Available Services:"
echo "• API Gateway: http://localhost:8080"
echo "• Travel Packages: http://localhost:8081/api/travel-packages"
echo "• Bookings: http://localhost:8082/api/bookings"
echo "• Notifications: http://localhost:8083/api/notifications/health"
echo ""
echo "📊 Sample API calls:"
echo "• Get all packages: curl http://localhost:8080/api/travel-packages"
echo "• Create booking: curl -X POST http://localhost:8080/api/bookings -H 'Content-Type: application/json' -d '{\"userId\":\"user123\",\"packageId\":\"1\",\"quantity\":2,\"packagePrice\":1299.99}'"
echo ""
echo "📖 Import 'STBP-API-Collection.postman_collection.json' in Postman for complete API testing"
echo ""
echo "🔧 Development commands:"
echo "• Build all: mvn clean package"
echo "• Run specific service: mvn spring-boot:run -pl api-gateway"
echo "• Clean all: mvn clean"
echo ""
