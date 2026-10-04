# Task List

A simple task manager built with Spring Boot. It provides a REST API for creating, listing, updating and deleting tasks, plus a small static web page (HTML/CSS/JS) served by the same app.

## Features

- Create, list, update and delete tasks
- Each task has a title, description, due date, status (`OPEN` / `COMPLETED`) and priority (`HIGH` / `MEDIUM` / `LOW`)
- Request validation and a global exception handler with consistent error responses
- In-memory H2 database, so there is nothing to install or configure

## Tech stack

- Java 25
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- H2 in-memory database
- Maven (wrapper included)

## Getting started

Run `./mvnw spring-boot:run` (on Windows: `.\mvnw.cmd spring-boot:run`), then open http://localhost:8080.

## API

Base path: `/api/v1/tasks`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/tasks` | Create a task |
| GET | `/api/v1/tasks` | List all tasks |
| PUT | `/api/v1/tasks/{taskId}` | Update a task |
| DELETE | `/api/v1/tasks/{taskId}` | Delete a task |

`taskId` is a UUID.

## Project structure

```
src/main/java/com/Ali/Task
├── controller   REST controller and exception handler
├── domain       entities, request objects and DTOs
├── mapper       entity <-> DTO mapping
├── repository   Spring Data JPA repository
├── service      business logic
└── exception    custom exceptions
src/main/resources
├── static       front end (index.html, app.js, style.css)
└── application.properties
```

## Configuration

The database password can be overridden with the `DB_PASSWORD` environment variable. It defaults to `password` for the in-memory H2 database.