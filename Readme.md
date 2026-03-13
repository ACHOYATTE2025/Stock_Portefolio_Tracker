# Stock Portfolio Tracker — Spring Boot REST API

## Overview

This is a RESTful API built to demonstrate proficiency in the Java programming language and the Spring Boot framework. Developed with Java 17 and Spring Boot 3, the backend features JWT-based authentication, JPA/Hibernate for relational database management, and integration with the Alpha Vantage external API to retrieve real-time stock prices. The application allows users to manage a stock portfolio — track holdings, execute buy/sell transactions, monitor wallet balance, and get stock recommendations based on real-time market data. The entire application is containerized with Docker and deployed on Render.

## Purpose

This project was developed to consolidate and demonstrate skills acquired during the BYU-Idaho Software Development Certificate program. The goal was to build a real-world REST API that covers the full backend development lifecycle: designing secured endpoints with Spring Security, managing relational data with PostgreSQL, integrating a third-party financial API, and deploying a production-ready containerized application using Docker and a CI/CD-ready workflow.

---

## Live Demo

| Service | URL |
|---------|-----|
| Backend API | https://stock-portfolio-tracker-app-backend.onrender.com |
| Swagger UI | https://stock-portfolio-tracker-app-backend.onrender.com/api/stockportefoliotracker/v1/swagger-ui/index.html |

> ⚠️ Render free tier puts the service to sleep after inactivity. The first request may take 30–60 seconds to wake up.

---

## Tech Stack

| Technology | Version | Role |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 3.5 | Framework |
| Spring Security | 6 | JWT Authentication |
| Spring Data JPA | 3.5 | ORM / Database |
| Hibernate | 6.6 | JPA Implementation |
| PostgreSQL | 16 | Relational Database |
| HikariCP | 6.3 | Connection Pooling |
| Caffeine | — | In-memory Cache |
| WebClient | — | HTTP Client (Alpha Vantage) |
| Docker | — | Containerization |
| Maven | 3.9 | Build Tool |

---

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/CSE310/Stock_Portefolio_Tracker/
│   │   │   ├── Config/          ← Security, CORS, Cache, WebClient config
│   │   │   ├── Controllers/     ← REST endpoints
│   │   │   ├── Dto/             ← Request/Response objects
│   │   │   ├── Entities/        ← JPA entities
│   │   │   ├── Enum/            ← TransactionType (BUY/SELL)
│   │   │   ├── ExternalApi/     ← Alpha Vantage client
│   │   │   ├── Repository/      ← Spring Data JPA repositories
│   │   │   ├── Security/        ← JWT filter, JWT service
│   │   │   └── Services/        ← Business logic
│   │   └── resources/
│   │       └── application.yml
├── Dockerfile                   ← Multi-stage: dev + build + prod
└── pom.xml
```

---

## API Endpoints

### Authentication
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/register` | Public |
| POST | `/login` | Public |
| POST | `/refreshtoken` | Public |

### Portfolio
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/portfolio` | Authenticated |
| POST | `/portfolio` | Authenticated |

### Transactions
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/transactions` | Authenticated |
| GET | `/transactions` | Authenticated |

### Wallet
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/wallet` | Authenticated |
| PUT | `/wallet/deposit` | Authenticated |

### Stocks & Recommendations
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/stocks` | Authenticated |
| GET | `/recommendations` | Authenticated |

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- Git

---

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/ACHOYATTE2025/Stock_Portefolio_Tracker.git
cd Stock_Portefolio_Tracker
```

### 2. Configure environment variables

Create a `.env` file at the root:

```env
POSTGRES_DB=stockdb
DB_USER=postgres
DB_PASS=your_password
JWT_KEY=your_jwt_secret
MAIL_USER=your_email
MAIL_PASS=your_email_password
ALPHA_VINTAGE=your_alpha_vantage_key
```

### 3. Run with Docker

```bash
docker compose up --build
```

| Service    | URL                   |
|------------|-----------------------|
| Backend    | http://localhost:8080 |
| PostgreSQL | localhost:5432        |

---

## Deploying to Render

### Steps

1. **Build and push the image to Docker Hub:**

```bash
docker build --target prod --no-cache -t achoyatte2025/stock_portfolio_tracker_app-backend:latest .
docker push achoyatte2025/stock_portfolio_tracker_app-backend:latest
```

2. **On Render**, create a Web Service → Existing Docker Image → `achoyatte2025/stock_portfolio_tracker_app-backend:latest`

3. **Set environment variables** on Render:

| Variable | Value |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<host>:5432/<dbname>` |
| `DB_USER` | your Render DB username |
| `DB_PASS` | your Render DB password |
| `JWT_KEY` | your JWT secret |
| `MAIL_USER` | your SMTP email |
| `MAIL_PASS` | your SMTP password |
| `ALPHA_VINTAGE` | your Alpha Vantage API key |

4. **Trigger Manual Deploy.**

### Important Notes

- The PostgreSQL URL from Render uses the format `postgresql://user:pass@host/db` — prefix it with `jdbc:` for Spring Boot: `jdbc:postgresql://host:5432/db`
- The backend uses Spring Boot Actuator — health check available at `/actuator/health`

---

## Spring Boot Configuration

In `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>:5432/<dbname>
    username: ${DB_USER}
    password: ${DB_PASS}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
```

---

## Security

- JWT tokens with configurable expiration
- Refresh token mechanism with `failedQueue` pattern
- Spring Security filter chain with stateless session
- CORS configured for both local and Render frontend origins

---

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | ✅ |
| `DB_USER` | Database username | ✅ |
| `DB_PASS` | Database password | ✅ |
| `JWT_KEY` | JWT signing secret | ✅ |
| `MAIL_USER` | SMTP email address | ✅ |
| `MAIL_PASS` | SMTP email password | ✅ |
| `ALPHA_VINTAGE` | Alpha Vantage API key | ✅ |

> ⚠️ **Never commit the `.env` file** — it is listed in `.gitignore`.

---

## Author

GitHub: [@ACHOYATTE2025](https://github.com/ACHOYATTE2025)
