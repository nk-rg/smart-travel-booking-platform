# Smart Travel Booking Platform (STBP)

A complete microservices-based travel booking platform built with Spring Boot, featuring real-time notifications, event-driven architecture, and containerized deployment. **Organized as a Maven parent project for easy development!**

## 🏗️ Architecture Overview

The platform consists of 4 main microservices organized in a **Maven parent project structure**:

- **API Gateway** (Port 8080) - Single entry point for all client requests
- **Travel Package Service** (Port 8081) - Manages travel packages, flights, and hotels
- **Booking Service** (Port 8082) - Handles booking operations and transactions
- **Notification Service** (Port 8083) - Sends email notifications for booking events

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.5.3, Java 17
- **Databases**: PostgreSQL (Travel Packages), MySQL (Bookings)
- **Message Broker**: Apache Kafka
- **API Gateway**: Spring Cloud Gateway
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven (Parent Project Structure)
- **IDE**: IntelliJ IDEA compatible

## 📂 Project Structure

```
smart-travel-booking-platform/          # Parent Maven project
├── pom.xml                             # Parent POM with all modules
├── api-gateway/                        # API Gateway module
│   ├── pom.xml                         # Child POM
│   └── src/main/java/stbp/apigateway/
├── travel-package-service/             # Travel Package Service module
│   ├── pom.xml                         # Child POM
│   └── src/main/java/stbp/travelpackageservice/
├── booking-service/                    # Booking Service module
│   ├── pom.xml                         # Child POM
│   └── src/main/java/stbp/travelpackageservice/bookingservice/
├── notification-service/               # Notification Service module
│   ├── pom.xml                         # Child POM
│   └── src/main/java/stbp/travelpackageservice/notificationservice/
├── user-service/                       # User Service module
│   ├── pom.xml                         # Child POM
│   └── src/main/java/stbp/travelpackageservice/userservice/
├── docker-compose.yml                  # Docker orchestration
├── setup.bat / setup.sh               # Complete setup scripts
└── README.md                          # This file
```

## 📦 Services Details

### API Gateway
- Routes requests to appropriate microservices
- Handles CORS configuration
- Load balancing and monitoring endpoints

### Travel Package Service
- **Framework**: Spring WebFlux (Reactive)
- **Database**: PostgreSQL with R2DBC
- **Features**:
  - CRUD operations for travel packages
  - Search functionality (by name, price range)
  - Kafka integration for booking events
  - Sample data with 5 pre-loaded packages

### Booking Service
- **Framework**: Spring Boot with JPA
- **Database**: MySQL
- **Features**:
  - Create, update, cancel bookings
  - User booking history
  - Booking confirmation workflow
  - Kafka event publishing

### Notification Service
- **Framework**: Spring Boot
- **Features**:
  - Kafka event consumption
  - Email notifications for booking events
  - Support for booking created/confirmed/cancelled events

## 🚀 Quick Start (Parent Project)

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- IntelliJ IDEA (recommended)

### 1. Open in IDE
```bash
# Clone or open the parent project
cd smart-travel-booking-platform

# Open in IntelliJ IDEA - it will automatically detect all modules
idea .
```

### 2. Build All Services (One Command!)
```bash
# Build all microservices from parent project
mvn clean package

# Or use the helper script
./setup.bat   # Windows
./setup.sh    # Linux/Mac
```

### 3. Start Everything
```bash
docker-compose up --build
```

### 4. Development Mode (Run Individual Services)
```bash
# Run specific service for development
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl travel-package-service
mvn spring-boot:run -pl booking-service
mvn spring-boot:run -pl notification-service

# Or run all services locally (different terminals)
mvn spring-boot:run -pl api-gateway &
mvn spring-boot:run -pl travel-package-service &
mvn spring-boot:run -pl booking-service &
mvn spring-boot:run -pl notification-service &
```

### 5. Verify Services
- API Gateway: http://localhost:8080
- Travel Packages: http://localhost:8081/api/travel-packages
- Bookings: http://localhost:8082/api/bookings
- Notifications: http://localhost:8083/api/notifications/health

## 🔧 Development Commands

### Maven Parent Project Commands
```bash
# Build all modules
mvn clean package

# Clean all modules
mvn clean

# Run tests for all modules
mvn test

# Install all modules to local repository
mvn install

# Run specific module
mvn spring-boot:run -pl api-gateway

# Build specific module
mvn clean package -pl travel-package-service

# Skip tests during build
mvn clean package -DskipTests
```

### Docker Commands
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up --build
```

### IntelliJ IDEA Integration
The project includes pre-configured run configurations:
- **All Services** - Run all 4 services at once
- **API Gateway** - Run only the gateway
- **Travel Package Service** - Run only the travel service
- **Booking Service** - Run only the booking service  
- **Notification Service** - Run only the notification service

## 📋 API Endpoints

### Travel Packages
```
GET    /api/travel-packages           # Get all packages
GET    /api/travel-packages/{id}      # Get package by ID
POST   /api/travel-packages           # Create new package
PUT    /api/travel-packages/{id}      # Update package
DELETE /api/travel-packages/{id}      # Delete package
GET    /api/travel-packages/search    # Search packages
```

### Bookings
```
GET    /api/bookings                  # Get all bookings
GET    /api/bookings/{id}             # Get booking by ID
GET    /api/bookings/user/{userId}    # Get user bookings
POST   /api/bookings                  # Create booking
PUT    /api/bookings/{id}             # Update booking
DELETE /api/bookings/{id}             # Delete booking
POST   /api/bookings/{id}/confirm     # Confirm booking
POST   /api/bookings/{id}/cancel      # Cancel booking
```

### Notifications
```
POST   /api/notifications/send        # Send custom notification
GET    /api/notifications/health      # Health check
```

## 🔧 Configuration

### Database Configuration
- **PostgreSQL**: Username: `postgres`, Password: `123456`, DB: `db_travelpackage`
- **MySQL**: Username: `user123`, Password: `123456`, DB: `bookings`

### Kafka Configuration
- Bootstrap servers: `localhost:29092,39092`
- Topic: `booking-events`
- Consumer groups: `travel-package-group`, `notification-group`

### Email Configuration (Notification Service)
Update `notification-service/src/main/resources/application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## 📊 Sample Data

The system comes pre-loaded with 5 travel packages:
1. **Paris Getaway** - $1,299.99
2. **Tokyo Adventure** - $2,499.99
3. **Bali Beach Resort** - $1,899.99
4. **Swiss Alps Ski Package** - $2,899.99
5. **Greek Islands Cruise** - $1,599.99

## 🔄 Event Flow

1. **Booking Created** → Kafka Event → Notification Service → Email Sent
2. **Booking Confirmed** → Kafka Event → Travel Package Service + Notification Service
3. **Booking Cancelled** → Kafka Event → Notification Service → Cancellation Email

## 🐛 Troubleshooting

### Common Issues

1. **Port Conflicts**: Make sure ports 8080-8083, 3306, 5432, and 29092-49092 are available
2. **Kafka Connection**: Wait for Kafka brokers to be fully started before starting services
3. **Database Connection**: Ensure PostgreSQL and MySQL containers are healthy before services start
4. **Maven Build Issues**: Run `mvn clean` then `mvn compile` if you encounter build problems

### Logs
```bash
# View all services logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f travel-package-service
docker-compose logs -f booking-service
docker-compose logs -f notification-service

# Maven logs for specific module
mvn spring-boot:run -pl api-gateway -X  # Debug mode
```

## 🧪 Testing

### Sample API Calls

#### Create a Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "packageId": "1",
    "quantity": 2,
    "packagePrice": 1299.99
  }'
```

#### Get All Travel Packages
```bash
curl http://localhost:8080/api/travel-packages
```

#### Search Packages by Price
```bash
curl "http://localhost:8080/api/travel-packages/search?minPrice=1000&maxPrice=2000"
```

## 🔒 Security Notes

- This is a development setup - add authentication/authorization for production
- Database passwords are hardcoded for demo purposes
- Email credentials should be properly secured
- Add input validation and error handling for production use

## 📈 Monitoring

Access actuator endpoints:
- Gateway Routes: http://localhost:8080/actuator/gateway/routes
- Health Checks: http://localhost:808{1,2,3}/actuator/health

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes and test using `mvn clean package`
4. Submit a pull request

## 📄 License

This project is for educational purposes. Use at your own discretion.

---

**Happy Coding with Maven Parent Project!** 🚀✈️

### 🎯 Key Advantages of Parent Project Structure:

✅ **Single IDE Window** - Open entire project in one IntelliJ IDEA window  
✅ **Unified Build** - Build all services with one `mvn clean package` command  
✅ **Dependency Management** - Centralized version management in parent POM  
✅ **Easy Development** - Pre-configured run configurations for all services  
✅ **Simplified CI/CD** - Single build pipeline for all microservices  
✅ **Better Organization** - Clear module structure with shared configurations
