# Hospital Management System

A Spring Boot hospital management web application with role-based access for administrators, doctors, and patients.

## Project Requirements

- Java 21 or newer
- Maven 3.9+ or the included Maven wrapper
- PostgreSQL 16+ for local development
- Docker and Docker Compose for containerized setup

## Tech Stack

- Spring Boot 4
- Spring MVC, Spring Security, Spring Data JPA
- Thymeleaf templates
- PostgreSQL
- H2 database for tests
- Docker Compose

## Configuration

The application reads configuration from environment variables and falls back to local defaults.

| Variable | Default | Purpose |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/hospital_db` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `123` | Database password |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Hibernate schema mode |
| `SERVER_PORT` | `8080` | Application port |
| `APP_SEED_ENABLED` | `true` | Seed demo users on startup |
| `APP_SEED_ADMIN_EMAIL` | `admin@hospital.com` | Demo admin email |
| `APP_SEED_ADMIN_PASSWORD` | `password` | Demo admin password |
| `APP_SEED_DOCTOR_EMAIL` | `sarah@hospital.com` | Demo doctor email |
| `APP_SEED_DOCTOR_PASSWORD` | `password` | Demo doctor password |

Copy `.env.example` if you want a local environment reference:

```bash
cp .env.example .env
```

On Windows PowerShell, set variables with:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="your-password"
```

## Run With Docker

Docker is the easiest way to start the full project because it runs PostgreSQL and the app together.

```bash
docker compose up --build
```

Open the app at:

```text
http://localhost:8080
```

Stop containers with:

```bash
docker compose down
```

To remove the PostgreSQL volume too:

```bash
docker compose down -v
```

## Run Locally

Create a PostgreSQL database:

```sql
CREATE DATABASE hospital_db;
```

Start the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

If Docker/PostgreSQL is not installed, run with the in-memory development database:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

## Test

Tests use an in-memory H2 database, so PostgreSQL is not required for `mvn test`.

```bash
mvn test
```

Or with the wrapper:

```bash
./mvnw test
```

## Demo Accounts

These accounts are created automatically when `APP_SEED_ENABLED=true`.

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@hospital.com` | `password` |
| Doctor | `sarah@hospital.com` | `password` |
| Patient | Register from `/register` | Your chosen password |

## Main Routes

- `/login` - login page
- `/register` - patient registration
- `/dashboard` - redirects users by role
- `/admin/dashboard` - admin dashboard
- `/doctor/dashboard` - doctor dashboard
- `/patient/dashboard` - patient dashboard
- `/appointment/book` - book appointment
- `/appointment/calendar` - appointment calendar

## Notes

- The database schema is managed by Hibernate with `spring.jpa.hibernate.ddl-auto=update` by default.
- For production, use strong passwords and set `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` or manage schema changes with migrations.
- The Docker image builds the application inside the container, so a prebuilt `target/*.jar` is not required.
