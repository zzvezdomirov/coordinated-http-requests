
# Coordinated HTTP Requests Service

This Spring Boot application coordinates multiple HTTP requests, where every 5th request depends on the successful completion of all previous requests. The application leverages `CompletableFuture` to manage the asynchronous behavior.

## Task Overview

- **Concurrency**: Executes a list of HTTP requests (up to 100) in parallel.
- **Dependency**: Every 5th request waits for the previous 4 requests to complete before starting.
- **Response Handling**: Only the response status is considered (no body processing or error handling is required).

## Features

- Executes HTTP requests concurrently while ensuring every 5th request waits for the previous ones.
- Implemented using Java, Spring Boot, and `CompletableFuture`.
- Basic unit tests to verify the execution flow and synchronization of requests.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/zzvezdomirov/coordinated-http-requests.git
   ```
2. Navigate to the project directory:
   ```bash
   cd coordinated-http-requests
   ```
3. Build and run the project using Maven:
   ```bash
   mvn spring-boot:run
   ```

The service will run at `http://localhost:8080`.

## Test the Service

To test the functionality, you can use a tool like `curl` to send a POST request to the endpoint:

```bash
curl -X POST "http://localhost:8080/execute-requests" -H "Content-Type: application/json" -d '["http://service1.com", "http://service2.com", "http://service3.com", "http://service4.com", "http://service5.com"]'

```

This will trigger the execution of the coordinated HTTP requests.

## Technologies Used

- **Java**: Backend programming language.
- **Spring Boot**: Framework for building the RESTful service.
- **CompletableFuture**: For handling asynchronous HTTP requests and managing dependencies.
- **JUnit 5**: Unit testing framework.
- **Mockito**: For mocking HTTP requests in tests.

## Testing

```bash
mvn test
```
