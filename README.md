# Sawah Tourism Application - Backend API

The backend API for the **Sawah System**, a comprehensive tourism platform. This project handles user management, authentication, service provider management, places of interest, and system infrastructure using modern Java technologies.

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot 3.4.3**
  - Spring Web
  - Spring Data JPA
  - Spring Security (with JWT)
  - Spring Boot Mail
  - Spring Data Redis
- **Database:** MySQL
- **Caching:** Redis
- **Tools & Libraries:** 
  - Lombok (Boilerplate reduction)
  - MapStruct (Object mapping)
  - SpringDoc OpenAPI / Swagger (API documentation)
  - JSON Web Tokens (JJWT)
  - P6Spy (SQL logging)

## 🌟 Core Features Implementation

### 1. User Management & Security
- **Authentication & Authorization:** Secure endpoints using JWT.
- **Roles:** Support for granular permissions including `Admin`, `Provider`, and `User` roles.
- **Profiles:** Custom user profiles with individual preferences and favorite places tracking.
- **Communication:** Integrated email services for notifications/verifications.

### 2. Service Provider Management
- **Provider Profiles:** Detailed management of service providers.
- **Services & Pricing:** Management of provider services along with place-specific pricing.
- **Provider Multi-Language Support:** Track which languages service providers speak (`ProviderLanguage`).
- **Reviews:** Dedicated review systems for users to rate providers and places.

### 3. Sawah Tourism Architecture
- **Places & Categories:** Comprehensive models for tourist places (`Place`, `PlacePhoto`, `PlacePrice`), properly nested within system `Category` domains.
- **Engagement:** Users can favorite places, log visited places, and submit reviews.
- **Chat System:** Integrated chat messaging system (`ChatConversation`, `ChatMessage`) to facilitate communication.
- **Internationalization (i18n):** Support for multi-language responses based on regions/languages (`Language`).

## 🛠️ Project Architecture

This application follows a robust multi-layered architecture:
- `controller/`: REST endpoints managing HTTP requests.
- `service/`: Core business logic layer (e.g. `EmailService`, `ProviderService`, etc).
- `repository/`: Spring Data JPA interfaces for database access.
- `models/`: JPA Entities mapping to MySQL tables.
- `dto/` & `mapper/`: Data Transfer Objects and MapStruct mappers shielding internal entities from the external API representation.
- `security/`: Custom JWT filters, Authentication providers, and configuration.
- `helper/` & `exceptions/`: Centralized utilities and exception handling.

## ⚙️ Setup and Installation

### Prerequisites
- JDK 17
- MySQL 8+
- Redis Server
- Maven (Embedded wrapper available in the project)

### Configuration
You will need to set up several environment variables (or configure them in `application.properties`) before running the application:

```properties
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
ADMIN_EMAIL=admin_account_email
ADMIN_PASSWORD=admin_account_password

# Redis Settings
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_USERNAME=default
REDIS_PASSWORD=

# Security
JWT_SECRET_KEY=your_secure_randomly_generated_base64_secret_key

# Email Server (SMTP)
MAIL_USERNAME=your_smtp_email
MAIL_PASSWORD=your_smtp_password
```

### Running the Application

1. Clone the repository and navigate into the backend folder.
2. Ensure MySQL and Redis are running.
3. Start the application using Maven Wrapper:

```bash
# Windows
./mvnw.cmd spring-boot:run

# MacOS / Linux
./mvnw spring-boot:run
```

The server will start on `http://localhost:9091` (or your configured `server.port`). 

*Note: The API base prefix is configured as `/api/v1`.*

## 📚 API Documentation
Once the server is running, the API documentation (Swagger UI) is automatically available through SpringDoc OpenAPI. Visit the Swagger documentation route (typically `http://localhost:9091/swagger-ui.html`) to test the endpoints interactively.
