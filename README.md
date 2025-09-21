# Gym CRM System

A modern, microservices-based Customer Relationship Management system designed for fitness centers and gyms. The system
handles training sessions, member management, and facility operations through a distributed architecture.

## Project Overview

This project consists of multiple microservices:

- Discovery Server - Service registry and discovery
- Gateway - API Gateway for routing and load balancing
- Gym Service - Core gym management functionality
- Training Session Service - Handles training schedules and bookings

## Technical Stack

- Java
- Spring Boot 3
- Gradle (with Kotlin DSL)
- Microservices Architecture
- Spring Cloud

## Requirements

- JDK 17 or higher
- Gradle 8.x
- Docker (optional, for containerization)

## Project Structure

```text
gym-crm/
├── discovery-server/          # Eureka Server
├── gateway/                   # Spring Cloud Gateway
├── gym-service/               # Core gym management
├── training-session-service/  # Training management
└── buildSrc/                  # Gradle build logic
```

## Getting Started

### Build the Project

```bash
./gradlew clean build
```

### Set Up Environment Variables

```text
DB_URL=jdbc:postgresql://localhost:5432/<your_database>
DB_USERNAME=postgres
DB_PASSWORD=<your_password>
JWT_SECRET_KEY=<your_secret_key>
JWT_LIFETIME=15 # in minutes
```

### Run the Services

Start the services in the following order:

1. Discovery Server:

```bash
./gradlew :discovery-server:bootRun
```

2. Gateway:

```bash
./gradlew :gateway:bootRun
```

3. Core Services:

```bash
./gradlew :gym-service:bootRun
./gradlew :training-session-service:bootRun
```

## Service Architecture

The system uses a microservices architecture with:

- Service Discovery (Eureka)
- API Gateway pattern
- Independent service deployment
- Distributed configuration

## Development

Each service can be developed and tested independently. The project uses:

- Gradle for dependency management and builds
- Spring Boot for microservices
- Lombok & MapStruct for reducing boilerplate
- JUnit & Mockito for testing

## Configuration

Configuration is managed through Spring Cloud Config. Main configuration files are located in the `src/main/resources`
directory of each service.

## Testing

Run tests using:

```bash
./gradlew test
```
## Project Status

This project is under active development. Features and APIs may change.

For detailed API documentation and service-specific information, refer to each service's individual documentation.

## Future plans

1. Add integration with Azure;
2. Replace `auth` module with Keycloak for `dev` ENV;
3. Add OpenAPI documentation for each service;
4. Implement CI/CD pipelines;
5. Replace in-memory DB with `MongoDB` in `training-session-service` module;
6. Add `docker-compose.yml` file for local deployment;
7. Refactor codebase according to DDD principles.

## Contributing

1. Create a feature branch
2. Commit your changes
3. Push to the branch
4. Create a Pull Request
