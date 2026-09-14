<p align="center">
  <h1 align="center">🧭 Sawah Backend</h1>
  <p align="center">
    <strong>The intelligent tourism platform that connects travelers with local guides, translators & drivers — powered by AI.</strong>
  </p>
  <p align="center">
    <a href="#"><img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"></a>
    <a href="#"><img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.5"></a>
    <a href="#"><img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"></a>
    <a href="#"><img src="https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"></a>
    <a href="#"><img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT"></a>
    <a href="#"><img src="https://img.shields.io/badge/Gemini_AI-2.5_Flash-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Gemini AI"></a>
    <a href="#"><img src="https://img.shields.io/badge/WebSocket-STOMP-010101?style=for-the-badge&logo=socketdotio&logoColor=white" alt="WebSocket"></a>
    <a href="#"><img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License"></a>
  </p>
</p>

---

## 📖 Overview

**Sawah** (سواح — Arabic for "Travelers") is a full-stack smart tourism platform designed to revolutionize how tourists explore destinations. The backend serves as the core API layer, orchestrating everything from AI-powered travel conversations to real-time booking notifications and intelligent landmark recognition.

### The Problem

Tourists in new destinations struggle to find reliable local guides, translators, and drivers. Language barriers, overpricing, and lack of personalized recommendations make travel planning stressful and fragmented.

### The Solution

Sawah brings together **AI intelligence**, **real-time communication**, and a **marketplace of verified local service providers** into a single, elegant API — enabling tourists to discover places, book services, and get AI-powered travel assistance in their preferred language.

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                        Client Apps                               │
│              (Mobile — Android / iOS / Web)                      │
└──────────────┬──────────────────────┬────────────────────────────┘
               │ REST / WebSocket     │
               ▼                      ▼
┌──────────────────────────┐  ┌──────────────────────────────────┐
│   Sawah Spring Boot API  │  │   WebSocket (STOMP over SockJS)  │
│       :9091              │  │   /ws-notifications              │
│                          │  │   Real-time push notifications   │
│  ┌────────────────────┐  │  └──────────────────────────────────┘
│  │  Security Layer    │  │
│  │  JWT + Google OAuth│  │
│  └────────────────────┘  │
│  ┌────────────────────┐  │  ┌──────────────────────────────────┐
│  │  Business Services │  │──│  FastAPI Microservice  :8000     │
│  │  28 service modules│  │  │  • /api/predict (Landmark CNN)   │
│  └────────────────────┘  │  │  • /api/recommend (ML Engine)    │
│  ┌────────────────────┐  │  └──────────────────────────────────┘
│  │  Spring AI          │  │
│  │  Gemini 2.5 Flash  │  │  ┌──────────────────────────────────┐
│  └────────────────────┘  │  │  External Services               │
└──────────┬───────────────┘  │  • Google OAuth 2.0              │
           │                   │  • SMTP Email (Password Reset)   │
           ▼                   │  • Email Validation API          │
┌──────────────────────────┐  └──────────────────────────────────┘
│   MySQL  │  Redis Cache  │
│  Primary │  TTL: 10min   │
│  Storage │  Performance  │
└──────────────────────────┘
```

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **API Gateway** | Spring Boot 3.5 + Spring Security | REST API, JWT auth, method-level RBAC |
| **Real-Time** | WebSocket + STOMP + SockJS | Push notifications for booking updates |
| **AI / ML** | Spring AI + Gemini 2.5 Flash | Chatbot, landmark info generation |
| **ML Microservice** | FastAPI (Python) | Landmark image recognition (CNN), place recommendations |
| **Persistence** | MySQL + Spring Data JPA | Primary relational storage |
| **Caching** | Redis | Response caching with 10-minute TTL |
| **Email** | Spring Mail + SMTP | OTP delivery, password reset flows |

---

## ✨ Core Features

### 🔐 Authentication & Security
- **JWT-based authentication** with access + refresh token rotation
- **Google OAuth 2.0** sign-in (web & mobile client IDs)
- **Role-Based Access Control** — `TOURIST`, `PROVIDER`, `ADMIN`
- **BCrypt password hashing** with secure password reset via email OTP
- **Method-level security** with `@PreAuthorize` across all endpoints
- **Stateless session management** (no server-side sessions)

### 🤖 AI-Powered Intelligence
- **Travel Chatbot** — Conversational AI powered by Gemini 2.5 Flash via Spring AI, with context-aware responses and auto-generated conversation titles
- **Landmark Recognition** — Upload a photo → FastAPI CNN model identifies the landmark → Gemini generates rich, localized information
- **Smart Recommendations** — ML-based place recommendation engine served via the FastAPI microservice

### 📍 Places & Discovery
- Full CRUD for tourist places with multi-photo uploads, categories, and pricing
- **Favorites** & **Visited Places** tracking per user
- **Recent Search** history with personalized recall
- **User Preferences** for tailored discovery experiences
- **Reviews & Ratings** system for places

### 🧑‍💼 Provider Marketplace
- Service provider registration with ID verification workflow (`PENDING` → `APPROVED` / `REJECTED`)
- Three service types: **Guide**, **Translator**, **Driver**
- Provider profiles with bio, experience, hourly/daily rates, language proficiency levels
- **Provider Reviews & Ratings** with aggregate scoring
- Availability management and provider search/filtering

### 📅 Booking & Service Requests
- Full booking lifecycle: `PENDING` → `ACCEPTED` → `COMPLETED` / `REJECTED` / `CANCELLED`
- Support for hourly & daily bookings with automatic price calculation
- Location-aware bookings with pickup coordinates
- Vehicle type preferences for driver bookings
- Provider response messages and timestamped status transitions

### 🔔 Real-Time Notifications
- **WebSocket (STOMP/SockJS)** push notifications for booking status changes
- JWT-authenticated WebSocket handshake (header & query parameter support)
- Persistent notification storage with read/unread tracking
- User-specific notification queues (`/user/{id}/queue/notifications`)

### 🌍 Internationalization (i18n)
- Full **English** and **Arabic** support across all API responses
- `Accept-Language` header-driven locale resolution
- Localized error messages, success messages, and notification content
- AI chatbot responds in the same language as the user's prompt

### 🛡️ Admin Dashboard
- Admin-only endpoints for platform management
- Provider approval/rejection workflow with reason tracking
- Issue/complaint management system with status tracking (`OPEN` → `IN_PROGRESS` → `RESOLVED` / `CLOSED`)

---

## 🛠️ Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java (OpenJDK) | 17 |
| **Framework** | Spring Boot | 3.5.14 |
| **Security** | Spring Security + JJWT | 0.11.5 |
| **Data** | Spring Data JPA + Hibernate | — |
| **Database** | MySQL | 8.x |
| **Caching** | Redis + Spring Cache | — |
| **AI** | Spring AI + Google Gemini | 1.1.5 |
| **Real-Time** | Spring WebSocket (STOMP) | — |
| **HTTP Client** | Spring WebFlux (WebClient) | — |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) | 2.8.5 |
| **Email** | Spring Mail (SMTP) | — |
| **Mapping** | MapStruct | 1.5.5 |
| **Boilerplate** | Lombok | 1.18.30 |
| **Auth (OAuth)** | Google API Client + Auth Library | 2.4.0 / 1.23.0 |
| **Monitoring** | Spring Actuator | — |
| **Build** | Maven Wrapper | — |
| **ML Microservice** | FastAPI (Python) | — |

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Minimum Version |
|------------|----------------|
| **JDK** | 17+ |
| **Maven** | 3.8+ (or use included `mvnw`) |
| **MySQL** | 8.0+ |
| **Redis** | 7.0+ |
| **FastAPI Service** | Running on `localhost:8000` (for AI/ML features) |

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/sawah-backend.git
cd sawah-backend
```

### 2. Configure Environment Variables

Create a `.env` file or set the following environment variables:

```properties
# ── Database ──────────────────────────────────────────────
DB_URL=jdbc:mysql://localhost:3306/sawah_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_db_password

# ── JWT Tokens ────────────────────────────────────────────
JWT_SECRET_KEY=your_base64_encoded_256bit_secret
JWT_EXPIRATION_TIME=3600000          # 1 hour (ms)
JWT_REFRESH_EXPIRATION_TIME=604800000 # 7 days (ms)

# ── Redis ─────────────────────────────────────────────────
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_USERNAME=default
REDIS_PASSWORD=your_redis_password

# ── Admin Account (auto-seeded on startup) ────────────────
ADMIN_EMAIL=admin@sawah.com
ADMIN_PASSWORD=your_admin_password

# ── Email (SMTP) ─────────────────────────────────────────
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# ── Email Validation ─────────────────────────────────────
VALIDATE_EMAIL_API_KEY=your_validation_api_key

# ── Google OAuth 2.0 ─────────────────────────────────────
GOOGLE_CLIENT_ID=your_web_client_id.apps.googleusercontent.com
GOOGLE_MOBILE_CLIENT_ID=your_mobile_client_id.apps.googleusercontent.com

# ── Gemini AI ─────────────────────────────────────────────
GEMINI_API_KEY=your_gemini_api_key
```

### 3. Build & Run

```bash
# Build the project (skip tests for faster startup)
./mvnw clean install -DskipTests

# Run the application
./mvnw spring-boot:run
```

> **Windows users:** Use `mvnw.cmd` instead of `./mvnw`.

The server starts on **`http://localhost:9091`**.

### 4. Verify the Setup

```bash
# Health check
curl http://localhost:9091/actuator/health

# Expected response:
# {"status":"UP"}
```

---

## 📚 API Documentation

Interactive Swagger UI is available once the server is running:

🔗 **[http://localhost:9091/swagger-ui/index.html](http://localhost:9091/swagger-ui/index.html)**

OpenAPI JSON spec: `http://localhost:9091/v3/api-docs`

### Key Endpoint Categories

| Prefix | Description | Auth Required |
|--------|------------|:------------:|
| `POST /api/v1/auth/**` | Login, Sign-up, Google OAuth, Password Reset, Token Refresh | ❌ |
| `GET/POST /api/v1/places/**` | Place CRUD, search, photos, pricing | ✅ |
| `GET/POST /api/v1/providers/**` | Provider profiles, search, availability | ✅ |
| `GET/POST /api/v1/bookings/**` | Service request lifecycle management | ✅ |
| `POST /api/v1/chats/messages` | AI chatbot conversations | ✅ (Tourist) |
| `POST /api/v1/landmarks/explore` | AI landmark recognition from image | ✅ (Tourist) |
| `GET /api/v1/notifications/me` | Real-time notification feed | ✅ |
| `GET/POST /api/v1/reviews/**` | Place reviews & ratings | ✅ |
| `GET/POST /api/v1/categories/**` | Place categories management | ✅ |
| `GET/POST /api/v1/services/**` | Service type management | ✅ |
| `GET /api/v1/users/**` | User profiles & preferences | ✅ |
| `GET /api/v1/admin/**` | Admin dashboard & provider approval | ✅ (Admin) |
| `WS /ws-notifications` | WebSocket (STOMP/SockJS) endpoint | JWT Token |

---

## 📂 Project Structure

```
sawah-backend/
├── src/main/java/com/sawah/sawah_backend/
│   ├── config/                  # App, Security, Redis, WebSocket, CORS configs
│   ├── controller/              # 18 REST controllers
│   ├── dto/                     # Request/Response DTOs
│   ├── enums/                   # Role, Status, ServiceCode, VehicleType enums
│   ├── exceptions/              # Custom exception classes + global handler
│   ├── helper/                  # Utility helpers
│   ├── mapper/                  # MapStruct mapper interfaces
│   ├── models/                  # 24 JPA entity classes
│   ├── repository/              # Spring Data JPA repositories
│   ├── requests/                # Request payload POJOs
│   ├── response/                # Response wrapper classes
│   ├── security/
│   │   ├── jwt/                 # JWT filter, utils, token management
│   │   └── user/                # CustomUserDetails, UserDetailsService
│   └── service/                 # 28 service packages (interface + impl)
│       ├── aiService/
│       │   ├── chatbot/         # Gemini-powered conversational AI
│       │   └── landmark/        # Image → Landmark → AI info pipeline
│       ├── auth/                # Authentication & token management
│       ├── booking/             # Service request lifecycle
│       ├── notification/        # WebSocket push notification engine
│       ├── recommendation/      # ML-based place recommendations
│       └── ...                  # category, place, provider, review, etc.
├── src/main/resources/
│   ├── application.properties   # Configuration (env-var driven)
│   ├── i18n/
│   │   ├── messages.properties      # English messages
│   │   └── messages_ar.properties   # Arabic messages (العربية)
│   └── templates/               # Email templates
├── pom.xml                      # Maven dependencies & build config
├── mvnw / mvnw.cmd              # Maven wrapper (no Maven install needed)
└── docs/                        # Additional documentation
```

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines to keep the codebase clean and consistent.

### Branching Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready releases |
| `develop` | Integration branch for features |
| `feature/<name>` | New feature development |
| `bugfix/<name>` | Bug fixes |
| `hotfix/<name>` | Urgent production fixes |

### Workflow

1. **Fork** the repository
2. **Create** a feature branch from `develop`:
   ```bash
   git checkout -b feature/awesome-feature develop
   ```
3. **Commit** your changes using [Conventional Commits](https://www.conventionalcommits.org/):
   ```
   feat(booking): add cancellation policy enforcement
   fix(auth): handle expired refresh token gracefully
   docs(readme): update environment variables section
   refactor(provider): extract rating calculation to service
   ```
4. **Push** to your fork and open a **Pull Request** against `develop`
5. Ensure your PR passes all CI checks before requesting review

### Conventional Commits Quick Reference

| Prefix | When to Use |
|--------|------------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation only changes |
| `style` | Formatting, missing semicolons, etc. |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement |
| `test` | Adding or correcting tests |
| `chore` | Build process, dependency updates, tooling |

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <sub>Built with ❤️ by the Sawah Team</sub>
</p>
