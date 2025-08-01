#!/bin/bash

echo "Building Smart Travel Booking Platform (Parent Project)..."
echo "========================================================="

# Build all modules from parent project
echo "Building all microservices from parent POM..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ All services built successfully!"
    echo ""
    echo "📋 Built modules:"
    echo "• api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar"
    echo "• travel-package-service/target/travel-package-service-0.0.1-SNAPSHOT.jar"
    echo "• booking-service/target/booking-service-0.0.1-SNAPSHOT.jar"
    echo "• notification-service/target/notification-service-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "🚀 Run 'docker-compose up --build' to start the platform"
else
    echo "❌ Build failed!"
    exit 1
fi
