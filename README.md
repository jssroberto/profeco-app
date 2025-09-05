# Profeco App

**Empowering consumers with transparent price comparison across retail establishments.**

## Overview

PROFECO App is a comprehensive price comparison platform inspired by Mexico's Procuraduría Federal del Consumidor (PROFECO). The system bridges the gap between consumers and retailers by providing real-time price transparency, enabling informed purchasing decisions while promoting fair market practices.

Our platform serves three distinct user groups: consumers seeking the best deals, stores wanting to showcase competitive pricing, and PROFECO administrators ensuring market fairness and consumer protection.

## Key Features

### For Consumers

- **Smart Product Search** - Find and compare prices across multiple stores instantly
- **Personalized Experience** - Save favorite products, stores, and create custom shopping lists
- **Price Analytics** - Track price history and receive alerts for price drops
- **Report Discrepancies** - Report price inconsistencies between published and actual store prices
- **Store Reviews** - Rate and comment on store experiences to help other consumers
- **Product Wishlist** - Request stores to carry specific products you're looking for
- **Real-time Notifications** - Get instant alerts for special offers and price changes

### For Stores (Supermarkets/Markets)

- **Customer Insights** - Access detailed reports on ratings, comments, and customer feedback
- **Price Management** - Easy upload and update of product prices and inventory
- **Targeted Promotions** - Create and publish special offers with real-time customer notifications
- **Discrepancy Reports** - Receive and manage customer-reported price inconsistencies
- **Demand Intelligence** - View customer wishlists to understand market demand
- **Performance Analytics** - Track store performance and customer satisfaction metrics

### For PROFECO Administrators

- **Market Oversight** - Monitor and verify price discrepancy reports across all establishments
- **Compliance Management** - Implement penalties and fines for stores with verified pricing violations
- **Market Analysis** - Generate comprehensive reports on market trends and pricing patterns
- **Consumer Protection** - Ensure fair trading practices and protect consumer rights

## System Architecture

PROFECO App follows a **microservices architecture** with **event-driven communication** to ensure scalability, maintainability, and real-time responsiveness. The system is designed as a distributed application where different services handle specific business domains.

### Architecture Components:

- **Frontend Service**: React-based single-page application with TypeScript
- **Backend Service**: Spring Boot application handling core business logic
- **Database Layer**: PostgreSQL for relational data storage
- **Message Broker**: RabbitMQ for asynchronous event processing and real-time notifications
- **File Storage**: Local file system for product images and documents

The services communicate through **REST APIs** for synchronous operations and **RabbitMQ message queues** for asynchronous events such as notifications.

![System Architecture Diagram](docs/architecture.png)

### Communication Flow:

1. **Synchronous**: Frontend ↔ Backend API (REST)
2. **Asynchronous**: Backend → RabbitMQ → Notification processing
3. **Data Flow**: Backend ↔ PostgreSQL for persistent storage

## Tech Stack

### Frontend

- React 19.0
- TypeScript 5.7
- Tailwind CSS 4.1
- Vite 6.2

### Backend

- Java 21
- Spring Boot 3.4.5
- Spring Security
- Apache Maven

### Database & Messaging

- PostgreSQL 17
- RabbitMQ 4.1

### DevOps & Tools

- Docker
- Docker Compose

### Additional Libraries

- **MapStruct** - Object mapping
- **Lombok** - Boilerplate code reduction
- **SpringDoc OpenAPI** - API documentation
- **JWT** - Authentication tokens
- **Axios** - HTTP client
- **Lucide React** - Icons

## Getting Started

### Prerequisites

Ensure you have the following software installed on your system:

- **Git** (latest version)
- **Docker** (24.0 or higher)
- **Docker Compose** (2.0 or higher)
- **Java 21** (for local development)
- **Node.js 18+** (for local development)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/jssroberto/profeco-app.git
   cd profeco-app
   ```

2. **Set up environment variables:**

   ```bash
   cp .env.example .env
   ```

   Edit the `.env` file with your configuration:

   ```env
   POSTGRES_DB=profeco_db
   DEFAULT_USER=your_username
   DEFAULT_PASSWORD=your_secure_password
   ```

3. **Build and run the entire system:**

   ```bash
   docker-compose up --build
   ```

4. **Access the application:**
   - **Frontend**: http://localhost:5173
   - **Backend API**: http://localhost:8080
   - **API Documentation**: http://localhost:8080/swagger-ui.html
   - **RabbitMQ Management**: http://localhost:15672

### Development Setup

For local development without Docker:

1. **Start PostgreSQL and RabbitMQ:**

   ```bash
   docker-compose up postgres rabbitmq
   ```

2. **Run the backend:**

   ```bash
   cd profeco-back
   ./mvnw spring-boot:run
   ```

3. **Run the frontend:**
   ```bash
   cd profeco-front
   npm install
   npm run dev
   ```

## API Endpoints (Examples)

### Authentication

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Product Search

```http
GET /api/products?search=milk&location=Mexico&sortBy=price&order=asc
```

### Price Discrepancy Report

```http
POST /api/stores/{storeId}/reports/discrepancy
Content-Type: application/json

{
  "productId": "123",
  "reportedPrice": 25.50,
  "actualPrice": 30.00,
  "description": "Price difference found during purchase",
  "evidence": "base64_image_data"
}
```

### Store Offers

```http
GET /api/stores/{storeId}/offers
```

### Customer Notifications

```http
POST /api/customers/{customerId}/notifications
Content-Type: application/json

{
  "type": "PRICE_ALERT",
  "productId": "123",
  "message": "Price dropped by 15%!"
}
```

## Project Structure

```
profeco-app/
├── profeco-back/
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── uploads/
│   ├── Dockerfile.dev
│   └── pom.xml
├── profeco-front/
│   ├── src/
│   ├── public/
│   ├── Dockerfile.dev
│   └── package.json
├── docs/
├── compose.yaml
└── README.md
```

### Backend Structure

- **Controllers**: REST API endpoints
- **Services**: Business logic implementation
- **Repositories**: Data access layer
- **Entities**: JPA data models
- **DTOs**: Data transfer objects
- **Config**: Security and application configuration

### Frontend Structure

- **Components**: Reusable React components
- **Pages**: Route-specific page components
- **Context**: State management
- **API**: HTTP client configuration
- **Assets**: Static resources

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
