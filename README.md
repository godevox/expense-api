# Godevox Expense API

This is a Spring Boot-based Expense API project. It includes various filters for request and response validation, uses Undertow as the embedded server.

## Table of Contents

- [Getting Started](#getting-started)
- [Filters](#filters)
- [Undertow Configuration](#undertow-configuration)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- PostgreSQL database

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/godevox/godevox-expense-api.git
    cd godevox-expense-api
    ```

2. Configure the database in `src/main/resources/application.properties`:
    ```ini
    spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
    spring.datasource.username=godevox
    spring.datasource.password=godevox
    ```

3. Build the project:
    ```sh
    mvn clean install
    ```

4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Filters

### RequestHeaderValidator

- **Description**: Validates the presence and format of the `X-Request-ID` header.
- **Order**: 1
- **URL Pattern**: `/*`

### RequestBodyValidator

- **Description**: Validates the request body size (max 1KB) for POST requests.
- **Order**: 2
- **URL Pattern**: `/*`

### ResponseHeaderFilter

- **Description**: Adds security headers to the response.
- **Order**: 3
- **URL Pattern**: `/*`

## Undertow Configuration

The application uses Undertow as the embedded server. The configuration is set in `src/main/resources/application.properties`:

```ini
server.undertow.io-threads=8
server.undertow.worker-threads=32
server.undertow.buffer-size=1024
server.undertow.direct-buffers=true
```

