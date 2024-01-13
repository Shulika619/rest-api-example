# Task Management System API

A simple example of REST API development and demonstration of working with popular technologies.

## Usage
> [!IMPORTANT]  
> The project makes use of Postgres Docker image for database support, so make sure you have the Docker Desktop running on your machine.


- `git clone git@github.com:Shulika619/rest-api-example.git`
- `cd rest-api-example`
- `mvnw spring-boot:run`

### Swagger(OpenApi) documentation for API
- `http://localhost:8080/swagger-ui/index.html`

### Spring Security (JWT)

Access to the API is authenticated using the jwt token

- `http://localhost:8080/api/v1/signup` to register and receive a token
- `http://localhost:8080/api/v1/signin` to login and receive a token

> [!NOTE]  
> More information can be found in the documentation

## Technologies
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data Jpa
- Postgresql
- Liquibase
- Validation
- JWT
- Cache
- OpenAPI Swagger
- Testcontainers
- Mapstruct
- Lombok
- Docker
- Maven
- Git/GitHub

