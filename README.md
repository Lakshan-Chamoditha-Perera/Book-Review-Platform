# Book Review Platform

A production-ready microservices-based book review platform built with Spring Boot 3.5.7 and Java 21, demonstrating enterprise-grade software architecture principles, design patterns, and security best practices.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Microservices](#microservices)
- [Security Architecture](#security-architecture)
- [Design Principles](#design-principles)
- [Design Patterns](#design-patterns)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Logging](#logging)
- [Testing](#testing)
- [Contributing](#contributing)
- [Future Enhancements](#future-enhancements)

---

## Overview

The Book Review Platform is a distributed system that allows users to browse books, write reviews, and rate books. The system is built using a microservices architecture, where each service is independently deployable and scalable.

### Key Features

- **User Management**: Registration, authentication, and profile management with role-based access control
- **Book Catalog**: Comprehensive book management with ISBN tracking
- **Review System**: User reviews and ratings with cross-service validation
- **Service Discovery**: Dynamic service registration and discovery with Netflix Eureka
- **API Gateway**: Unified entry point with JWT authentication and CORS support
- **Security**: BCrypt password hashing and JWT-based stateless authentication
- **Centralized Logging**: Structured logging across all services
- **RESTful API**: Clean, consistent API design following REST principles

---

## Architecture

### Microservices Architecture

The platform follows a **microservices architecture** pattern, decomposing the application into loosely coupled, independently deployable services. This architecture enables:

- **Independent Scaling**: Each service can be scaled based on its specific load
- **Technology Diversity**: Services can use different technologies as needed
- **Fault Isolation**: Failures in one service don't cascade to others
- **Independent Deployment**: Services can be deployed without affecting others
- **Team Autonomy**: Different teams can own different services

```
┌─────────────────────────────────────────────────────────────┐
│                         Client                              │
│                    (Web/Mobile App)                         │
└────────────────────────┬────────────────────────────────────┘
                         │
                         │ HTTPS
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              API Gateway (Port 9000)                        │
│              ┌─────────────────────────────┐                │
│              │  - JWT Authentication       │                │
│              │  - Request Routing          │                │
│              │  - Load Balancing           │                │
│              │  - CORS Configuration       │                │
│              │  - Rate Limiting (Future)   │                │
│              └─────────────────────────────┘                │
└────────────┬────────────┬────────────┬──────────────────────┘
             │            │            │
             ▼            ▼            ▼
┌────────────────┐ ┌────────────┐ ┌──────────────┐
│  User Service  │ │Book Service│ │Review Service│
│  (Dynamic Port)│ │(Dynamic Port)│(Dynamic Port)│
│                │ │            │ │              │
│  - Auth/Login  │ │ - CRUD Ops │ │ - CRUD Ops   │
│  - User CRUD   │ │ - ISBN Mgmt│ │ - Validation │
│  - JWT Gen     │ │            │ │ - WebClient  │
└────────┬───────┘ └─────┬──────┘ └──────┬───────┘
         │               │               │
         │               │               │
         └───────────────┴───────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │  Discovery Service (Eureka)   │
         │        (Port 8761)            │
         │                               │
         │  - Service Registry           │
         │  - Health Monitoring          │
         │  - Load Balancing Info        │
         └───────────────────────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │      MySQL Databases          │
         │  ┌─────────────────────────┐  │
         │  │  users_db               │  │
         │  │  books_db               │  │
         │  │  reviews_db             │  │
         │  └─────────────────────────┘  │
         └───────────────────────────────┘
```

### Service Communication

- **Synchronous Communication**: REST APIs using Spring WebClient with client-side load balancing
- **Service Discovery**: Netflix Eureka for dynamic service registration and discovery
- **API Gateway**: Spring Cloud Gateway (WebFlux-based) for routing and cross-cutting concerns
- **Load Balancing**: Client-side load balancing using Spring Cloud LoadBalancer

---

## Technology Stack

### Core Technologies

- **Java 21**: Latest LTS version with modern language features (records, pattern matching, virtual threads)
- **Spring Boot 3.5.7**: Enterprise application framework with auto-configuration
- **Spring Cloud 2025.0.0**: Microservices infrastructure and patterns
- **Maven**: Build automation and dependency management

### Spring Ecosystem

- **Spring Web**: RESTful web services with embedded Tomcat
- **Spring Data JPA**: Data persistence layer with repository pattern
- **Spring Cloud Gateway**: Reactive API Gateway implementation
- **Spring Cloud Netflix Eureka**: Service discovery and registration
- **Spring WebFlux**: Reactive programming for gateway and inter-service communication
- **Spring Actuator**: Production-ready features (health checks, metrics, monitoring)
- **Spring Security**: Authentication and authorization framework

### Database & Persistence

- **MySQL 8**: Relational database with ACID compliance
- **Hibernate**: ORM framework for object-relational mapping
- **JPA**: Java Persistence API for standardized data access

### Security

- **BCrypt**: Password hashing algorithm with automatic salt generation
- **JJWT (Java JWT)**: JWT token generation and validation
- **HS256 Algorithm**: HMAC with SHA-256 for JWT signing

### Additional Libraries

- **Lombok**: Boilerplate code reduction (@Data, @Builder, @RequiredArgsConstructor)
- **ModelMapper**: Object-to-object mapping for DTOs
- **SLF4J/Logback**: Logging facade and implementation

---

## Microservices

### 1. Discovery Service (Port 8761)

**Purpose**: Service registry for dynamic service discovery

**Technology**: Netflix Eureka Server

**Responsibilities**:

- Register all microservice instances with their metadata
- Provide service location information to clients
- Enable client-side load balancing
- Health monitoring of registered services
- Maintain service registry with heartbeat mechanism

**Key Configuration**:

```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
server.port=8761
```

**Access**: http://localhost:8761 (Eureka Dashboard)

---

### 2. API Gateway Service (Port 9000)

**Purpose**: Single entry point for all client requests with security enforcement

**Technology**: Spring Cloud Gateway (WebFlux-based, reactive)

**Responsibilities**:

- Route requests to appropriate microservices
- JWT token validation for protected endpoints
- Load balancing across service instances
- CORS configuration for web clients
- Cross-cutting concerns (authentication, logging, rate limiting)
- Request/response transformation

**Routes**:

- `/api/v1/auth/**` → User Service (Public - No authentication)
- `/api/v1/users/**` → User Service (Protected - JWT required)
- `/api/v1/books/**` → Book Service (Protected - JWT required)
- `/api/v1/reviews/**` → Review Service (Protected - JWT required)

**Key Features**:

- Load-balanced routing using `lb://` protocol
- CORS enabled for `http://localhost:3000`
- Reactive, non-blocking architecture
- JWT authentication filter
- Public endpoint configuration

**Security Configuration**:

```properties
jwt.secret=<BASE64-encoded-256-bit-secret>
api.public.endpoints=/api/v1/auth/**,/actuator/**,/eureka/**
```

---

### 3. User Service (Dynamic Port)

**Purpose**: Manage user accounts, authentication, and authorization

**Database**: `users_db`

**Endpoints**:

**Authentication (Public)**:

- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Authenticate user and get JWT token

**User Management (Protected)**:

- `GET /api/v1/users` - Get all users
- `POST /api/v1/users` - Create new user
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/email/{email}` - Get user by email
- `DELETE /api/v1/users/{id}` - Delete user

**Domain Model**:

```java
UserEntity {
    UUID id
    String username (unique, indexed)
    String password (BCrypt hashed, never exposed)
    String email (unique, indexed)
    List<Role> roles (ADMIN, USER)
}
```

**Key Features**:

- **BCrypt Password Hashing**: 10 rounds, automatic salt generation
- **JWT Token Generation**: Includes username, userId, and role claims
- **Email Uniqueness Validation**: Prevents duplicate accounts
- **Username Uniqueness Validation**: Ensures unique usernames
- **ModelMapper Integration**: DTO conversion
- **Global Exception Handling**: Consistent error responses
- **Role-Based Access Control**: ADMIN and USER roles

**Security Highlights**:

- Passwords never returned in API responses
- Generic error messages to prevent username enumeration
- Transactional registration process
- Password validation before storage

---

### 4. Book Service (Dynamic Port)

**Purpose**: Manage book catalog and metadata

**Database**: `books_db`

**Endpoints**:

- `GET /api/v1/books` - Get all books
- `POST /api/v1/books` - Add new book
- `GET /api/v1/books/{id}` - Get book by ID
- `DELETE /api/v1/books/{id}` - Delete book

**Domain Model**:

```java
Book {
    UUID id
    String title
    String author
    String isbn (unique)
    String description
}
```

**Key Features**:

- ISBN validation and uniqueness
- Full-text search capability (future)
- Comprehensive book metadata
- Global exception handling

---

### 5. Review Service (Dynamic Port)

**Purpose**: Manage book reviews and ratings with cross-service validation

**Database**: `reviews_db`

**Endpoints**:

- `GET /api/v1/reviews` - Get all reviews
- `POST /api/v1/reviews` - Create review
- `GET /api/v1/reviews/{id}` - Get review by ID
- `GET /api/v1/reviews/book/{bookId}` - Get reviews for a book
- `GET /api/v1/reviews/user/{userId}` - Get reviews by a user
- `DELETE /api/v1/reviews/{id}` - Delete review

**Domain Model**:

```java
Review {
    UUID id
    Integer rating (1-5)
    UUID bookId (foreign reference)
    UUID userId (foreign reference)
}
```

**Key Features**:

- **Inter-service Communication**: Validates book and user existence before creating review
- **WebClient with Load Balancing**: Calls User and Book services
- **Comprehensive Error Handling**: Network failures, service unavailability
- **Data Enrichment**: Returns review with user and book details
- **Referential Integrity**: Ensures valid foreign keys

**Service Integration**:

```java
// Validates book exists
bookWebClient.get().uri("/{id}", bookId)
    .retrieve()
    .bodyToMono(StandardResponse.class)
    .block();

// Validates user exists
userWebClient.get().uri("/{id}", userId)
    .retrieve()
    .bodyToMono(StandardResponse.class)
    .block();
```

---

## Security Architecture

### Overview

The platform implements a comprehensive security architecture with multiple layers of protection:

1. **Password Security**: BCrypt hashing with automatic salt generation
2. **Authentication**: JWT-based stateless authentication
3. **Authorization**: Role-based access control (RBAC)
4. **API Gateway Security**: Centralized authentication enforcement
5. **Secure Communication**: HTTPS-ready configuration

### Password Encryption Mechanism

#### BCrypt Algorithm

The system uses **BCrypt** for password hashing, which is specifically designed for password storage and provides:

- **Adaptive Hashing**: Configurable work factor (cost) that can be increased as hardware improves
- **Automatic Salt Generation**: Each password gets a unique random salt
- **One-Way Function**: Computationally infeasible to reverse
- **Slow by Design**: Intentionally slow to prevent brute-force attacks

#### Implementation Details

**Configuration**:

```java
@Component
public class PasswordEncoder {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordEncoder() {
        // 10 rounds = 2^10 = 1024 iterations
        // Good balance between security and performance
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
    }
}
```

**BCrypt Hash Structure**:

```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
└─┬─┘└┬┘└──────────┬──────────┘└──────────┬──────────┘
  │   │           │                       │
  │   │           │                       └─ Hash (31 chars)
  │   │           └─ Salt (22 chars, embedded)
  │   └─ Cost factor (10 = 2^10 rounds)
  └─ Algorithm version ($2a, $2b, or $2y)
```

**Key Features**:

- **Embedded Salt**: Salt is stored within the hash itself (no separate salt field needed)
- **60 Character Output**: Fixed-length hash string
- **Unique Hashes**: Same password produces different hashes due to random salts
- **Verification**: Extracts salt from hash to verify passwords

**Password Hashing Flow**:

```java
// Registration
String rawPassword = "userPassword123";
String hashedPassword = passwordEncoder.encode(rawPassword);
// Result: $2a$10$abc...xyz (60 chars, includes salt)

// Login Verification
boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
// BCrypt extracts salt from hash and verifies
```

**Security Benefits**:

- **Rainbow Table Resistance**: Unique salts prevent precomputed hash attacks
- **Brute Force Resistance**: Slow hashing makes brute force impractical
- **Future-Proof**: Cost factor can be increased as hardware improves
- **No Plain Text Storage**: Passwords never stored in plain text

### JWT (JSON Web Token) Authentication

#### Overview

The system uses **JWT** for stateless authentication, eliminating the need for server-side session storage. This approach provides:

- **Stateless**: No session storage required on server
- **Scalable**: Easy to scale horizontally
- **Cross-Service**: Token valid across all microservices
- **Self-Contained**: Token contains all necessary user information

#### JWT Structure

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImpvaG4iLCJ1c2VySWQiOiIxMjM0NSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYzMjU0MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
└──────────────────┬──────────────────┘ └────────────────────────────────────────────────────────────────────────────────────────────────────────┘ └─────────────────────┬─────────────────────┘
              HEADER                                                                    PAYLOAD                                                                                    SIGNATURE
```

**Header**:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload (Claims)**:

```json
{
  "username": "john_doe",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "USER",
  "iat": 1703001600,
  "exp": 1703088000
}
```

**Signature**:

```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

#### Implementation Details

**Token Generation** (User Service):

```java
public String generateToken(String username, UUID userId, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username);
    claims.put("userId", userId.toString());
    claims.put("role", role);

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
        .signWith(getSigningKey())
        .compact();
}
```

**Token Validation** (API Gateway):

```java
public Boolean validateToken(String token) {
    try {
        Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token);
        return !isTokenExpired(token);
    } catch (Exception e) {
        return false;
    }
}
```

**Secret Key Configuration**:

```properties
# Must be BASE64-encoded and at least 256 bits (32 bytes)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000  # 24 hours in milliseconds
```

**Security Considerations**:

- **Secret Key**: 256-bit minimum, BASE64-encoded, shared across services
- **Algorithm**: HS256 (HMAC with SHA-256)
- **Expiration**: 24-hour token lifetime
- **Validation**: Signature and expiration checked on every request
- **Claims**: Username, userId, and role for authorization

#### Authentication Flow

```
┌──────────┐                 ┌─────────────┐                 ┌──────────────┐
│  Client  │                 │ API Gateway │                 │ User Service │
└────┬─────┘                 └──────┬──────┘                 └──────┬───────┘
     │                              │                               │
     │ 1. POST /auth/register       │                               │
     │ {username, email, password}  │                               │
     ├─────────────────────────────>│                               │
     │                              │ 2. Forward request            │
     │                              ├──────────────────────────────>│
     │                              │                               │
     │                              │                               │ 3. Hash password
     │                              │                               │    with BCrypt
     │                              │                               │
     │                              │                               │ 4. Save user
     │                              │                               │    to database
     │                              │                               │
     │                              │ 5. Success response           │
     │                              │<──────────────────────────────┤
     │ 6. Registration successful   │                               │
     │<─────────────────────────────┤                               │
     │                              │                               │
     │ 7. POST /auth/login          │                               │
     │ {email, password}            │                               │
     ├─────────────────────────────>│                               │
     │                              │ 8. Forward request            │
     │                              ├──────────────────────────────>│
     │                              │                               │
     │                              │                               │ 9. Find user
     │                              │                               │    by email
     │                              │                               │
     │                              │                               │ 10. Verify password
     │                              │                               │     with BCrypt
     │                              │                               │
     │                              │                               │ 11. Generate JWT
     │                              │                               │     with claims
     │                              │                               │
     │                              │ 12. Return JWT token          │
     │                              │<──────────────────────────────┤
     │ 13. {token, username, role}  │                               │
     │<─────────────────────────────┤                               │
     │                              │                               │
     │ 14. GET /books               │                               │
     │ Header: Authorization:       │                               │
     │         Bearer <JWT>         │                               │
     ├─────────────────────────────>│                               │
     │                              │ 15. Validate JWT              │
     │                              │     - Verify signature        │
     │                              │     - Check expiration        │
     │                              │     - Extract claims          │
     │                              │                               │
     │                              │ 16. Forward to Book Service   │
     │                              │     (if valid)                │
     │                              │                               │
     │ 17. Return books data        │                               │
     │<─────────────────────────────┤                               │
     │                              │                               │
```

### Role-Based Access Control (RBAC)

**Roles**:

```java
public enum Role {
    USER,   // Regular users - can read and create reviews
    ADMIN   // Administrators - full access to all resources
}
```

**Authorization Flow**:

1. JWT token contains role claim
2. API Gateway extracts role from token
3. Role-based routing and access control (future enhancement)
4. Services can check role for fine-grained permissions

**Future Enhancements**:

- Method-level security with `@PreAuthorize`
- Resource-based permissions
- Dynamic role assignment
- Permission hierarchies

---

## Design Principles

### SOLID Principles

The codebase strictly adheres to SOLID principles for maintainable, extensible software:

#### 1. Single Responsibility Principle (SRP)

**Definition**: A class should have only one reason to change.

**Implementation**:

**Controllers** - Handle HTTP layer only:

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    // No business logic - delegates to service layer
}
```

**Services** - Business logic only:

```java
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public StandardResponse getUserById(UUID id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return StandardResponse.success(modelMapper.map(user, UserDTO.class));
    }
    // No HTTP concerns - pure business logic
}
```

**Repositories** - Data access only:

```java
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    // Only database operations
}
```

**DTOs** - Data transfer only:

```java
@Data
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    // No business logic - pure data
}
```

**Benefits**:

- Easy to test each layer independently
- Changes in one layer don't affect others
- Clear separation of concerns
- Easier to understand and maintain

#### 2. Open/Closed Principle (OCP)

**Definition**: Software entities should be open for extension but closed for modification.

**Implementation**:

**Interface-based design**:

```java
public interface UserService {
    StandardResponse getAllUsers();
    StandardResponse saveUser(UserRequestDTO dto);
    StandardResponse getUserById(UUID id);
}

@Service
public class UserServiceImpl implements UserService {
    // Implementation can be extended without modifying interface
}

// Can add new implementations without changing existing code:
// - UserServiceCachedImpl (adds caching)
// - UserServiceAsyncImpl (adds async processing)
// - UserServiceAuditedImpl (adds audit logging)
```

**Strategy Pattern for extensibility**:

```java
@Bean
public WebClient bookWebClient(WebClient.Builder builder) {
    return builder
        .baseUrl("http://bookservice/api/v1/books")
        .filter(/* can add filters without modifying core */)
        .build();
}
```

**Benefits**:

- New features added without modifying existing code
- Reduces risk of breaking existing functionality
- Promotes code reuse through composition

#### 3. Liskov Substitution Principle (LSP)

**Definition**: Objects of a superclass should be replaceable with objects of a subclass without breaking the application.

**Implementation**:

**Service substitutability**:

```java
// Any implementation of UserService can replace another
UserService service = new UserServiceImpl(repo, mapper);
UserService service = new UserServiceCachedImpl(repo, mapper, cache);
// Both work identically from client perspective
```

**Repository substitutability**:

```java
// Spring provides JpaRepository implementation
// Can be replaced with custom implementation or mock for testing
UserRepository repo = // Spring implementation
UserRepository repo = // Custom implementation
UserRepository repo = // Mock for testing
```

**Benefits**:

- Polymorphism works correctly
- Easy to swap implementations
- Facilitates testing with mocks

#### 4. Interface Segregation Principle (ISP)

**Definition**: Clients should not be forced to depend on interfaces they don't use.

**Implementation**:

**Focused interfaces**:

```java
// UserService only contains user-related operations
public interface UserService {
    StandardResponse getAllUsers();
    StandardResponse saveUser(UserRequestDTO dto);
    // No book or review methods mixed in
}

// BookService only contains book-related operations
public interface BookService {
    StandardResponse getAllBooks();
    StandardResponse saveBook(BookRequestDTO dto);
    // No user or review methods mixed in
}
```

**Separate DTOs for different purposes**:

```java
// Request DTO - only fields needed for creation
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
}

// Response DTO - only fields for response (no password)
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
}

// Login DTO - only fields needed for authentication
public class LoginRequest {
    private String email;
    private String password;
}
```

**Benefits**:

- Clients only depend on methods they use
- Reduces coupling
- Easier to understand and maintain

#### 5. Dependency Inversion Principle (DIP)

**Definition**: High-level modules should not depend on low-level modules. Both should depend on abstractions.

**Implementation**:

**Constructor injection with interfaces**:

```java
@Service
@RequiredArgsConstructor  // Lombok generates constructor
public class UserServiceImpl implements UserService {
    // Depends on abstractions, not concrete implementations
    private final UserRepository userRepository;      // Interface
    private final ModelMapper modelMapper;            // Abstraction
    private final PasswordEncoder passwordEncoder;    // Abstraction
    private final JwtUtil jwtUtil;                    // Abstraction
}
```

**Service layer depends on repository interface**:

```java
// High-level service depends on abstraction
private final UserRepository userRepository;  // Interface
// Not on concrete JPA implementation
// Spring provides implementation at runtime
```

**WebClient abstraction**:

```java
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    // Depends on WebClient abstraction
    private final WebClient bookWebClient;
    private final WebClient userWebClient;
    // Not on concrete HTTP client implementation
}
```

**Benefits**:

- Loose coupling between layers
- Easy to swap implementations
- Facilitates testing with mocks
- Promotes interface-based programming

---

### KISS (Keep It Simple, Stupid)

**Definition**: Systems work best when they are kept simple rather than made complicated.

**Implementation**:

1. **Simple REST endpoints**:

```java
@GetMapping("/{id}")
public ResponseEntity<StandardResponse> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserById(id));
}
// Clear, concise, easy to understand
```

2. **Straightforward service methods**:

```java
@Override
public StandardResponse getUserById(UUID id) {
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    return StandardResponse.success(modelMapper.map(user, UserDTO.class));
}
// No unnecessary complexity
```

3. **Clear naming conventions**:

- Controllers: `UserController`, `BookController`, `ReviewController`
- Services: `UserService`, `UserServiceImpl`
- Repositories: `UserRepository`, `BookRepository`
- DTOs: `UserDTO`, `UserRequestDTO`, `LoginRequest`
- Entities: `UserEntity`, `Book`, `Review`

4. **Minimal configuration** (Convention over configuration):

```properties
spring.application.name=userservice
server.port=0  # Let Spring choose available port
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```

5. **Standard HTTP methods** (REST conventions):

- GET for retrieval
- POST for creation
- PUT/PATCH for updates
- DELETE for deletion

**Benefits**:

- Easy to understand and maintain
- Reduces cognitive load
- Fewer bugs
- Faster onboarding for new developers

---

### DRY (Don't Repeat Yourself)

**Definition**: Every piece of knowledge must have a single, unambiguous, authoritative representation within a system.

**Implementation**:

1. **StandardResponse wrapper** (Reused across all services):

```java
@Data
@Builder
public class StandardResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String error;

    // Factory methods prevent duplication
    public static <T> StandardResponse<T> success(T data) {
        return StandardResponse.<T>builder()
            .status(200)
            .success(true)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static <T> StandardResponse<T> error(String error) {
        return StandardResponse.<T>builder()
            .status(500)
            .success(false)
            .error(error)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
```

2. **Global exception handling** (Centralized error handling):

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardResponse<Object>> handleUserNotFoundException(
        UserNotFoundException ex) {
        // Single place for handling this exception type
        return ResponseEntity.status(404)
            .body(StandardResponse.error(ex.getMessage()));
    }
}
```

3. **Parent POM for dependency management**:

```xml
<parent>
    <groupId>com.bookreviewplatform</groupId>
    <artifactId>BookReviewPlatform</artifactId>
    <version>1.0.0</version>
</parent>
<!-- All services inherit common dependencies -->
```

4. **Reusable WebClient configuration**:

```java
@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    // Reused across all services for inter-service communication
}
```

5. **Common logging configuration** (Shared across all services):

```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
logging.file.max-size=10MB
logging.file.max-history=30
```

6. **Lombok annotations** (Eliminate boilerplate):

```java
@Data                    // Generates getters, setters, equals, hashCode, toString
@Builder                 // Generates builder pattern
@NoArgsConstructor       // Generates no-args constructor
@AllArgsConstructor      // Generates all-args constructor
@RequiredArgsConstructor // Generates constructor for final fields
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
}
```

**Benefits**:

- Reduces code duplication
- Single source of truth
- Easier to maintain and update
- Consistent behavior across services

---

### Additional Design Principles

#### Separation of Concerns

Each layer has a distinct responsibility:

- **Presentation Layer**: HTTP handling, request/response mapping
- **Service Layer**: Business logic, transaction management
- **Data Access Layer**: Database operations, query execution
- **Domain Layer**: Business entities, domain models

#### Fail Fast

Validate inputs early and throw exceptions immediately:

```java
if (request.getUsername() == null || request.getUsername().isBlank()) {
    throw new IllegalArgumentException("Username is required");
}
```

#### Defensive Programming

Always validate inputs and handle edge cases:

```java
public String encode(String rawPassword) {
    if (rawPassword == null) {
        throw new IllegalArgumentException("Raw password cannot be null");
    }
    return bCryptPasswordEncoder.encode(rawPassword);
}
```

---

## Design Patterns

### 1. Layered Architecture Pattern

**Purpose**: Separate concerns into distinct layers

**Implementation**:

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│     (Controllers)                   │
│     - UserController                │
│     - BookController                │
│     - ReviewController              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Service Layer                   │
│     (Business Logic)                │
│     - UserServiceImpl               │
│     - BookServiceImpl               │
│     - ReviewServiceImpl             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Data Access Layer               │
│     (Repositories)                  │
│     - UserRepository                │
│     - BookRepository                │
│     - ReviewRepository              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Database Layer                  │
│     (MySQL)                         │
│     - users_db                      │
│     - books_db                      │
│     - reviews_db                    │
└─────────────────────────────────────┘
```

**Benefits**:

- Clear separation of concerns
- Easy to test each layer independently
- Changes in one layer don't affect others
- Promotes code reuse

---

### 2. Repository Pattern

**Purpose**: Encapsulate data access logic and provide abstraction over data sources

**Implementation**:

```java
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
```

**Benefits**:

- Abstracts database operations
- Easy to switch data sources (MySQL → PostgreSQL → MongoDB)
- Simplifies testing with mocks
- Provides standard CRUD operations out of the box
- Query methods generated from method names

---

### 3. Data Transfer Object (DTO) Pattern

**Purpose**: Transfer data between layers without exposing internal domain models

**Implementation**:

```java
// Request DTO - for incoming data
@Data
@Builder
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
}

// Response DTO - for outgoing data (no password)
@Data
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    // Password intentionally excluded
}

// Entity - internal domain model
@Entity
@Data
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;  // Never exposed in DTO
    private String email;
    private List<Role> roles;
}
```

**Benefits**:

- **Security**: Hide sensitive fields (passwords, internal IDs)
- **Decoupling**: API contract independent of database schema
- **Flexibility**: Different views of same data for different use cases
- **Validation**: Separate validation rules for input/output
- **Versioning**: Can maintain multiple DTO versions for API versioning

---

### 4. Service Layer Pattern

**Purpose**: Encapsulate business logic and provide transaction boundaries

**Implementation**:

```java
public interface UserService {
    StandardResponse getAllUsers();
    StandardResponse saveUser(UserRequestDTO dto);
    StandardResponse getUserById(UUID id);
    StandardResponse deleteUser(UUID id);
}

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StandardResponse saveUser(UserRequestDTO dto) {
        // Business logic: validation, transformation, persistence
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        UserEntity entity = modelMapper.map(dto, UserEntity.class);
        entity.setPassword(hashedPassword);
        UserEntity saved = userRepository.save(entity);
        return StandardResponse.success(modelMapper.map(saved, UserDTO.class));
    }
}
```

**Benefits**:

- Centralized business logic
- Transaction management with `@Transactional`
- Reusable across multiple controllers
- Easy to test with mocked dependencies
- Clear business operations

---

### 5. Dependency Injection Pattern

**Purpose**: Achieve loose coupling and testability through inversion of control

**Implementation**:

```java
@Service
@RequiredArgsConstructor  // Lombok generates constructor
public class UserServiceImpl implements UserService {
    // Dependencies injected via constructor
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
}

// Spring automatically injects dependencies
// No manual instantiation needed
```

**Benefits**:

- Loose coupling between components
- Easy to mock dependencies for testing
- Promotes interface-based programming
- Spring manages object lifecycle
- Constructor injection ensures immutability

---

### 6. API Gateway Pattern

**Purpose**: Single entry point for all client requests with centralized cross-cutting concerns

**Implementation**:

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("userservice", r -> r.path("/api/v1/users/**")
                .uri("lb://userservice"))
            .route("bookservice", r -> r.path("/api/v1/books/**")
                .uri("lb://bookservice"))
            .route("reviewservice", r -> r.path("/api/v1/reviews/**")
                .uri("lb://reviewservice"))
            .build();
    }
}
```

**Benefits**:

- Single entry point for clients
- Centralized authentication and authorization
- Load balancing across service instances
- Service abstraction (clients don't know about individual services)
- Cross-cutting concerns (logging, rate limiting, caching)
- Protocol translation (HTTP to gRPC, REST to GraphQL)

---

### 7. Service Registry Pattern (Service Discovery)

**Purpose**: Dynamic service location and health monitoring

**Implementation**:

```java
// Discovery Service
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryserviceApplication.class, args);
    }
}

// Client Services
@EnableDiscoveryClient
@SpringBootApplication
public class UserserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }
}
```

**Benefits**:

- Dynamic service registration
- Automatic load balancing
- Health monitoring and heartbeat
- No hard-coded service URLs
- Supports horizontal scaling
- Automatic failover

---

### 8. Builder Pattern

**Purpose**: Construct complex objects step by step with fluent API

**Implementation**:

```java
@Builder
public class UserEntity {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private List<Role> roles;
}

// Usage
UserEntity user = UserEntity.builder()
    .username("john_doe")
    .password("hashedPassword")
    .email("john@example.com")
    .roles(List.of(Role.USER))
    .build();
```

**Benefits**:

- Readable object construction
- Immutable objects
- Optional parameters
- Fluent API
- Compile-time safety

---

### 9. Factory Pattern (Static Factory Methods)

**Purpose**: Create objects without exposing creation logic

**Implementation**:

```java
public class StandardResponse<T> {
    public static <T> StandardResponse<T> success(T data) {
        return StandardResponse.<T>builder()
            .status(200)
            .success(true)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static <T> StandardResponse<T> error(String error) {
        return StandardResponse.<T>builder()
            .status(500)
            .success(false)
            .error(error)
            .timestamp(LocalDateTime.now())
            .build();
    }
}

// Usage
return StandardResponse.success(userData);
return StandardResponse.error("User not found");
```

**Benefits**:

- Descriptive method names
- Encapsulates object creation
- Can return cached instances
- Type inference
- Consistent object creation

---

### 10. Adapter Pattern (ModelMapper)

**Purpose**: Convert interface of a class into another interface

**Implementation**:

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;

    public StandardResponse saveUser(UserRequestDTO dto) {
        // Adapt DTO to Entity
        UserEntity entity = modelMapper.map(dto, UserEntity.class);

        UserEntity saved = userRepository.save(entity);

        // Adapt Entity to DTO
        UserDTO responseDto = modelMapper.map(saved, UserDTO.class);
        return StandardResponse.success(responseDto);
    }
}
```

**Benefits**:

- Automatic field mapping
- Reduces boilerplate code
- Type-safe conversions
- Configurable mapping strategies

---

### 11. Template Method Pattern (Spring Framework)

**Purpose**: Define skeleton of algorithm, let subclasses override steps

**Implementation**:

```java
// Spring's JpaRepository provides template
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    // Spring implements standard CRUD operations
    // We only define custom queries
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
```

**Benefits**:

- Reusable algorithm structure
- Consistent behavior
- Reduces code duplication
- Framework handles common operations

---

### 12. Proxy Pattern (Spring AOP)

**Purpose**: Provide surrogate or placeholder for another object

**Implementation**:

```java
@Transactional  // Spring creates proxy for transaction management
public StandardResponse saveUser(UserRequestDTO dto) {
    // Transaction automatically managed by proxy
}

@LoadBalanced  // Spring creates proxy for load balancing
public WebClient.Builder webClientBuilder() {
    // Load balancing automatically handled by proxy
}
```

**Benefits**:

- Transparent cross-cutting concerns
- No code modification needed
- Separation of concerns
- Declarative programming

---

### 13. Strategy Pattern (Future Enhancement)

**Purpose**: Define family of algorithms, make them interchangeable

**Implementation** (Example for future):

```java
public interface PasswordHashingStrategy {
    String hash(String password);
    boolean verify(String password, String hash);
}

public class BCryptStrategy implements PasswordHashingStrategy {
    // BCrypt implementation
}

public class Argon2Strategy implements PasswordHashingStrategy {
    // Argon2 implementation
}

// Can switch strategies without changing client code
```

---

## Getting Started

### Prerequisites

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/mysql/))
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Git** for version control

### Installation

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd BookReviewPlatform
```

#### 2. Configure MySQL Databases

Create the required databases:

```sql
CREATE DATABASE users_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE books_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE reviews_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Create a MySQL user (optional but recommended):

```sql
CREATE USER 'bookreview'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON users_db.* TO 'bookreview'@'localhost';
GRANT ALL PRIVILEGES ON books_db.* TO 'bookreview'@'localhost';
GRANT ALL PRIVILEGES ON reviews_db.* TO 'bookreview'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. Update Database Credentials

Update `application.properties` in each service:

**userservice/src/main/resources/application.properties**:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/users_db?createDatabaseIfNotExist=true
spring.datasource.username=bookreview
spring.datasource.password=secure_password
```

**bookservice/src/main/resources/application.properties**:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/books_db?createDatabaseIfNotExist=true
spring.datasource.username=bookreview
spring.datasource.password=secure_password
```

**reviewservice/src/main/resources/application.properties**:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reviews_db?createDatabaseIfNotExist=true
spring.datasource.username=bookreview
spring.datasource.password=secure_password
```

#### 4. Configure JWT Secret (IMPORTANT)

**Generate a secure 256-bit secret**:

```bash
# Using OpenSSL
openssl rand -base64 32

# Using Python
python -c "import secrets; print(secrets.token_urlsafe(32))"
```

**Update JWT secret in**:

- `userservice/src/main/resources/application.properties`
- `apigatewayservice/src/main/resources/application.properties`

```properties
# Replace with your generated secret
jwt.secret=YOUR_GENERATED_BASE64_SECRET_HERE
jwt.expiration=86400000
```

**⚠️ Security Warning**: Never commit real secrets to version control. Use environment variables or secret management systems in production.

#### 5. Build the Project

```bash
# Build all services
mvn clean install

# Or build individual services
cd userservice && mvn clean install
cd bookservice && mvn clean install
cd reviewservice && mvn clean install
cd discoveryservice && mvn clean install
cd apigatewayservice && mvn clean install
```

### Running the Services

**⚠️ Important**: Start services in the correct order to ensure proper registration.

#### Option 1: Manual Startup (Recommended for Development)

**Step 1: Start Discovery Service** (must start first)

```bash
cd discoveryservice
mvn spring-boot:run
```

Wait until you see: `Started DiscoveryserviceApplication`
Access Eureka Dashboard: http://localhost:8761

**Step 2: Start Microservices** (can start in parallel)

Terminal 2:

```bash
cd userservice
mvn spring-boot:run
```

Terminal 3:

```bash
cd bookservice
mvn spring-boot:run
```

Terminal 4:

```bash
cd reviewservice
mvn spring-boot:run
```

Wait for all services to register with Eureka (check dashboard).

**Step 3: Start API Gateway** (start last)

```bash
cd apigatewayservice
mvn spring-boot:run
```

Access API Gateway: http://localhost:9000

#### Option 2: Using JAR Files (Production-like)

```bash
# Build JARs
mvn clean package

# Start services
java -jar discoveryservice/target/discoveryservice-1.0.0.jar &
sleep 30  # Wait for Eureka to start

java -jar userservice/target/userservice-1.0.0.jar &
java -jar bookservice/target/bookservice-1.0.0.jar &
java -jar reviewservice/target/reviewservice-1.0.0.jar &
sleep 20  # Wait for services to register

java -jar apigatewayservice/target/apigatewayservice-1.0.0.jar &
```

#### Option 3: Using Docker (Future Enhancement)

```bash
docker-compose up -d
```

### Verify Services

#### 1. Check Eureka Dashboard

Visit http://localhost:8761 and verify all services are registered:

- ✅ USERSERVICE
- ✅ BOOKSERVICE
- ✅ REVIEWSERVICE
- ✅ APIGATEWAYSERVICE

#### 2. Health Check Endpoints

```bash
# Discovery Service
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:9000/actuator/health

# Individual services (check Eureka for dynamic ports)
curl http://localhost:<dynamic-port>/actuator/health
```

#### 3. Test API Gateway Routing

```bash
# Should return 401 Unauthorized (no token)
curl http://localhost:9000/api/v1/users

# Register a user (public endpoint)
curl -X POST http://localhost:9000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

---

## API Documentation

### Base URL

All requests go through the API Gateway:

```
http://localhost:9000
```

### Authentication Flow

#### 1. Register a New User

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "role": "USER"
}
```

**Response** (201 Created):

```json
{
  "status": 201,
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": null,
    "username": "john_doe",
    "role": "USER",
    "message": "User registered successfully. Please login to get your token."
  },
  "timestamp": "2025-12-22T10:30:00",
  "error": null
}
```

#### 2. Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123!"
}
```

**Response** (200 OK):

```json
{
  "status": 200,
  "success": true,
  "message": "Authentication successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "john_doe",
    "role": "USER",
    "message": "Login successful"
  },
  "timestamp": "2025-12-22T10:31:00",
  "error": null
}
```

**Save the token** for subsequent requests.

### User Service Endpoints

#### Get All Users

```http
GET /api/v1/users
Authorization: Bearer <your-jwt-token>
```

**Response**:

```json
{
  "status": 200,
  "success": true,
  "message": null,
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "username": "john_doe",
      "email": "john@example.com"
    }
  ],
  "timestamp": "2025-12-22T10:32:00",
  "error": null
}
```

#### Get User by ID

```http
GET /api/v1/users/{id}
Authorization: Bearer <your-jwt-token>
```

#### Get User by Email

```http
GET /api/v1/users/email/{email}
Authorization: Bearer <your-jwt-token>
```

#### Delete User

```http
DELETE /api/v1/users/{id}
Authorization: Bearer <your-jwt-token>
```

### Book Service Endpoints

#### Get All Books

```http
GET /api/v1/books
Authorization: Bearer <your-jwt-token>
```

#### Create Book

```http
POST /api/v1/books
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "description": "A handbook of agile software craftsmanship"
}
```

**Response** (201 Created):

```json
{
  "status": 201,
  "success": true,
  "message": "Book created successfully",
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "description": "A handbook of agile software craftsmanship"
  },
  "timestamp": "2025-12-22T10:33:00",
  "error": null
}
```

#### Get Book by ID

```http
GET /api/v1/books/{id}
Authorization: Bearer <your-jwt-token>
```

#### Delete Book

```http
DELETE /api/v1/books/{id}
Authorization: Bearer <your-jwt-token>
```

### Review Service Endpoints

#### Get All Reviews

```http
GET /api/v1/reviews
Authorization: Bearer <your-jwt-token>
```

#### Create Review

```http
POST /api/v1/reviews
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "rating": 5,
  "bookId": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response** (201 Created):

```json
{
  "status": 201,
  "success": true,
  "message": "Review created successfully",
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "rating": 5,
    "bookId": "660e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "book": {
      "title": "Clean Code",
      "author": "Robert C. Martin"
    },
    "user": {
      "username": "john_doe"
    }
  },
  "timestamp": "2025-12-22T10:34:00",
  "error": null
}
```

#### Get Review by ID

```http
GET /api/v1/reviews/{id}
Authorization: Bearer <your-jwt-token>
```

#### Get Reviews by Book

```http
GET /api/v1/reviews/book/{bookId}
Authorization: Bearer <your-jwt-token>
```

#### Get Reviews by User

```http
GET /api/v1/reviews/user/{userId}
Authorization: Bearer <your-jwt-token>
```

#### Delete Review

```http
DELETE /api/v1/reviews/{id}
Authorization: Bearer <your-jwt-token>
```

### Error Responses

#### 400 Bad Request

```json
{
  "status": 400,
  "success": false,
  "message": "Validation failed",
  "data": null,
  "timestamp": "2025-12-22T10:35:00",
  "error": "Username is required"
}
```

#### 401 Unauthorized

```json
{
  "status": 401,
  "success": false,
  "message": "Authentication failed",
  "data": null,
  "timestamp": "2025-12-22T10:35:00",
  "error": "Invalid credentials"
}
```

#### 404 Not Found

```json
{
  "status": 404,
  "success": false,
  "message": "Resource not found",
  "data": null,
  "timestamp": "2025-12-22T10:35:00",
  "error": "User not found with id: 550e8400-e29b-41d4-a716-446655440000"
}
```

#### 409 Conflict

```json
{
  "status": 409,
  "success": false,
  "message": "Duplicate resource",
  "data": null,
  "timestamp": "2025-12-22T10:35:00",
  "error": "Email already registered: john@example.com"
}
```

#### 500 Internal Server Error

```json
{
  "status": 500,
  "success": false,
  "message": "Internal server error",
  "data": null,
  "timestamp": "2025-12-22T10:35:00",
  "error": "Database connection failed"
}
```

---

## Database Schema

### users_db

```sql
CREATE TABLE user_entity (
    id BINARY(16) PRIMARY KEY COMMENT 'UUID stored as binary',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Unique username',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt hashed password (60 chars)',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Unique email address',
    roles VARCHAR(255) COMMENT 'Comma-separated roles (USER, ADMIN)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Indexes**:

- Primary key on `id` for fast lookups
- Unique index on `username` for uniqueness constraint
- Unique index on `email` for uniqueness constraint
- Index on `username` for login queries
- Index on `email` for email-based queries

### books_db

```sql
CREATE TABLE book (
    id BINARY(16) PRIMARY KEY COMMENT 'UUID stored as binary',
    title VARCHAR(255) NOT NULL COMMENT 'Book title',
    author VARCHAR(255) NOT NULL COMMENT 'Book author',
    isbn VARCHAR(20) UNIQUE COMMENT 'International Standard Book Number',
    description TEXT COMMENT 'Book description',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_isbn (isbn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Indexes**:

- Primary key on `id`
- Unique index on `isbn`
- Index on `title` for search queries
- Index on `author` for author-based queries

### reviews_db

```sql
CREATE TABLE review (
    id BINARY(16) PRIMARY KEY COMMENT 'UUID stored as binary',
    rating INT NOT NULL COMMENT 'Rating (1-5)',
    book_id BINARY(16) NOT NULL COMMENT 'Foreign key to books_db.book.id',
    user_id BINARY(16) NOT NULL COMMENT 'Foreign key to users_db.user_entity.id',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_book_id (book_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating),
    CONSTRAINT chk_rating CHECK (rating >= 1 AND rating <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Indexes**:

- Primary key on `id`
- Index on `book_id` for book-based queries
- Index on `user_id` for user-based queries
- Index on `rating` for rating-based queries
- Check constraint on `rating` (1-5)

**Note**: Foreign key constraints are not enforced at database level due to microservices architecture. Referential integrity is maintained at application level through service-to-service validation.

### Database Design Decisions

1. **UUID as Primary Key**:

   - Globally unique across distributed systems
   - No coordination needed between services
   - Prevents ID collision in distributed environment
   - Stored as BINARY(16) for efficiency

2. **Separate Databases per Service**:

   - Database per service pattern
   - Independent scaling
   - Service autonomy
   - Fault isolation

3. **No Foreign Key Constraints**:

   - Microservices architecture principle
   - Services maintain referential integrity
   - Allows independent deployment
   - Prevents cross-database dependencies

4. **UTF8MB4 Character Set**:

   - Full Unicode support
   - Supports emojis and special characters
   - International character support

5. **Timestamps**:
   - Audit trail
   - Track creation and modification times
   - Useful for debugging and analytics

---

## Configuration

### Service Ports

| Service           | Port        | Type                  | Access                |
| ----------------- | ----------- | --------------------- | --------------------- |
| Discovery Service | 8761        | Fixed                 | http://localhost:8761 |
| API Gateway       | 9000        | Fixed                 | http://localhost:9000 |
| User Service      | Dynamic (0) | Random available port | Via Eureka/Gateway    |
| Book Service      | Dynamic (0) | Random available port | Via Eureka/Gateway    |
| Review Service    | Dynamic (0) | Random available port | Via Eureka/Gateway    |

### Dynamic Port Assignment

Services use `server.port=0` to allow Spring Boot to assign random available ports. This enables:

- **Multiple Instances**: Run multiple instances of the same service
- **No Port Conflicts**: Automatic port selection prevents conflicts
- **Horizontal Scaling**: Easy to scale services horizontally
- **Development Flexibility**: No need to manage port assignments

Service discovery (Eureka) tracks actual ports and provides them to clients via the service registry.

### Environment-Specific Configuration

#### Development (application.properties)

```properties
spring.jpa.show-sql=true
logging.level.root=DEBUG
spring.jpa.hibernate.ddl-auto=update
```

#### Production (application-prod.properties)

```properties
spring.jpa.show-sql=false
logging.level.root=INFO
spring.jpa.hibernate.ddl-auto=validate
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

Activate profile:

```bash
java -jar userservice.jar --spring.profiles.active=prod
```

### Security Configuration

#### JWT Configuration

```properties
# User Service & API Gateway (must match)
jwt.secret=<BASE64-encoded-256-bit-secret>
jwt.expiration=86400000  # 24 hours

# API Gateway - Public endpoints
api.public.endpoints=/api/v1/auth/**,/actuator/**,/eureka/**
```

#### CORS Configuration (API Gateway)

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

### Database Configuration

#### Connection Pool (HikariCP)

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

#### JPA/Hibernate

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
```

### Eureka Configuration

#### Server (Discovery Service)

```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=true
eureka.server.eviction-interval-timer-in-ms=60000
```

#### Client (All Services)

```properties
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
eureka.instance.preferIpAddress=true
eureka.instance.lease-renewal-interval-in-seconds=30
eureka.instance.lease-expiration-duration-in-seconds=90
```

---

## Logging

### Log Levels

Each service uses the following log levels:

- **ROOT**: INFO (production), DEBUG (development)
- **Application Packages**: DEBUG
- **Spring Web**: INFO
- **Hibernate SQL**: DEBUG (development), INFO (production)
- **Spring Cloud Gateway**: DEBUG (API Gateway only)
- **Spring Security**: DEBUG (API Gateway only)

### Log Configuration

```properties
# Log Levels
logging.level.root=INFO
logging.level.com.bookreviewplatform=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG

# Console Pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# File Pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# File Configuration
logging.file.name=logs/userservice.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Log Files

Each service maintains its own log file:

```
BookReviewPlatform/
├── discoveryservice/logs/discoveryservice.log
├── apigatewayservice/logs/apigatewayservice.log
├── userservice/logs/userservice.log
├── bookservice/logs/bookservice.log
└── reviewservice/logs/reviewservice.log
```

### Log Rotation

- **Max Size**: 10MB per file
- **Max History**: 30 days
- **Compression**: Older logs compressed as `.gz`
- **Naming**: `servicename.log.YYYY-MM-DD.0.gz`

### Structured Logging (Future Enhancement)

```java
@Slf4j
@Service
public class UserServiceImpl {
    public StandardResponse saveUser(UserRequestDTO dto) {
        log.info("Creating user: username={}, email={}",
            dto.getUsername(), dto.getEmail());

        try {
            // Business logic
            log.debug("User created successfully: id={}", savedUser.getId());
        } catch (Exception e) {
            log.error("Failed to create user: username={}, error={}",
                dto.getUsername(), e.getMessage(), e);
        }
    }
}
```

### Centralized Logging (Future Enhancement)

Integration with ELK Stack (Elasticsearch, Logstash, Kibana):

```yaml
# docker-compose.yml
services:
  elasticsearch:
    image: elasticsearch:8.11.0
  logstash:
    image: logstash:8.11.0
  kibana:
    image: kibana:8.11.0
```

---

## Testing

### Unit Testing

Each service includes unit tests for:

- Service layer business logic
- Repository layer data access
- Utility classes (PasswordEncoder, JwtUtil)

**Example**:

```java
@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserById_Success() {
        // Given
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
            .id(userId)
            .username("testuser")
            .build();

        when(userRepository.findById(userId))
            .thenReturn(Optional.of(user));

        // When
        StandardResponse response = userService.getUserById(userId);

        // Then
        assertTrue(response.isSuccess());
        verify(userRepository).findById(userId);
    }
}
```

### Integration Testing

Test complete request-response cycles:

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateUser() throws Exception {
        mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"email\":\"test@example.com\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific service
cd userservice && mvn test

# Run with coverage
mvn test jacoco:report

# Skip tests during build
mvn clean install -DskipTests
```

### Test Coverage Goals

- **Service Layer**: >80% coverage
- **Controller Layer**: >70% coverage
- **Utility Classes**: >90% coverage
- **Overall**: >75% coverage

---

## Contributing

### Code Style

- **Java Naming Conventions**: Follow standard Java naming conventions
- **Lombok**: Use Lombok annotations to reduce boilerplate
- **Javadoc**: Write comprehensive Javadoc for public APIs
- **Single Responsibility**: Keep methods small and focused (SRP)
- **Meaningful Names**: Use descriptive variable and method names
- **Constants**: Use constants for magic numbers and strings
- **Exception Handling**: Always handle exceptions appropriately

### Code Formatting

```java
// Good
public StandardResponse getUserById(UUID id) {
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    return StandardResponse.success(modelMapper.map(user, UserDTO.class));
}

// Bad
public StandardResponse getUserById(UUID id){
UserEntity u=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));
return StandardResponse.success(modelMapper.map(u,UserDTO.class));}
```

### Testing Requirements

- Write unit tests for all service methods
- Write integration tests for controllers
- Mock external dependencies
- Aim for >80% code coverage
- Test edge cases and error conditions

### Git Workflow

1. **Create Feature Branch**:

   ```bash
   git checkout -b feature/add-book-search
   ```

2. **Make Changes**:

   ```bash
   git add .
   git commit -m "feat: add book search functionality"
   ```

3. **Push to Remote**:

   ```bash
   git push origin feature/add-book-search
   ```

4. **Create Pull Request**:

   - Provide clear description
   - Reference related issues
   - Request code review

5. **Code Review**:

   - Address review comments
   - Update PR as needed

6. **Merge**:
   - Squash and merge
   - Delete feature branch

### Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add book search endpoint
fix: resolve JWT token expiration issue
docs: update API documentation
refactor: simplify user service logic
test: add unit tests for password encoder
chore: update dependencies
```

### Pull Request Template

```markdown
## Description

Brief description of changes

## Type of Change

- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing

- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Checklist

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests pass locally
```

---

## Future Enhancements

### Security

- [x] **BCrypt Password Hashing**: Implemented with 10 rounds
- [x] **JWT Authentication**: Stateless authentication with HS256
- [x] **Role-Based Access Control**: USER and ADMIN roles
- [ ] **OAuth2/OpenID Connect**: Social login integration
- [ ] **API Key Authentication**: For service-to-service communication
- [ ] **Rate Limiting**: Prevent abuse and DDoS attacks
- [ ] **HTTPS/TLS**: Secure communication in production
- [ ] **Security Headers**: HSTS, CSP, X-Frame-Options
- [ ] **Input Validation**: Comprehensive validation with Bean Validation
- [ ] **SQL Injection Prevention**: Parameterized queries (already using JPA)

### Resilience

- [ ] **Circuit Breaker**: Resilience4j integration for fault tolerance
- [ ] **Retry Mechanism**: Automatic retry for transient failures
- [ ] **Timeout Configuration**: Request timeout handling
- [ ] **Bulkhead Pattern**: Isolate resources for different operations
- [ ] **Fallback Methods**: Graceful degradation
- [ ] **Health Checks**: Advanced health indicators

### Monitoring & Observability

- [ ] **Distributed Tracing**: Zipkin/Jaeger for request tracing
- [ ] **Metrics**: Micrometer/Prometheus for metrics collection
- [ ] **Centralized Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- [ ] **Dashboards**: Grafana for visualization
- [ ] **Alerting**: Alert on critical metrics
- [ ] **APM**: Application Performance Monitoring

### Data Management

- [ ] **Database Migration**: Flyway/Liquibase for version control
- [ ] **Caching**: Redis for performance optimization
- [ ] **Message Queue**: RabbitMQ/Kafka for async communication
- [ ] **Event-Driven Architecture**: Domain events and event sourcing
- [ ] **CQRS**: Command Query Responsibility Segregation
- [ ] **Read Replicas**: Database read scaling

### API Enhancements

- [ ] **API Versioning**: Support multiple API versions
- [ ] **Swagger/OpenAPI**: Interactive API documentation
- [ ] **GraphQL**: Flexible query language
- [ ] **WebSocket**: Real-time updates and notifications
- [ ] **Pagination**: Efficient large dataset handling
- [ ] **Filtering & Sorting**: Advanced query capabilities
- [ ] **Bulk Operations**: Batch create/update/delete

### DevOps & Deployment

- [ ] **Docker**: Containerization for all services
- [ ] **Docker Compose**: Local development environment
- [ ] **Kubernetes**: Container orchestration
- [ ] **Helm Charts**: Kubernetes package management
- [ ] **CI/CD Pipeline**: GitHub Actions/Jenkins
- [ ] **Infrastructure as Code**: Terraform/CloudFormation
- [ ] **Blue-Green Deployment**: Zero-downtime deployments
- [ ] **Canary Releases**: Gradual rollout

### Performance

- [ ] **Connection Pooling**: Optimized database connections
- [ ] **Query Optimization**: Indexed queries and N+1 prevention
- [ ] **Lazy Loading**: Efficient data fetching
- [ ] **Compression**: Response compression (gzip)
- [ ] **CDN**: Static asset delivery
- [ ] **Load Testing**: JMeter/Gatling performance tests

### Features

- [ ] **Book Search**: Full-text search with Elasticsearch
- [ ] **Review Comments**: Nested comments on reviews
- [ ] **User Profiles**: Extended user information
- [ ] **Book Recommendations**: ML-based recommendations
- [ ] **Reading Lists**: User-curated book lists
- [ ] **Social Features**: Follow users, share reviews
- [ ] **Notifications**: Email/push notifications
- [ ] **Admin Dashboard**: Management interface

---

## Troubleshooting

### Common Issues

#### 1. Services Not Registering with Eureka

**Symptoms**: Services don't appear in Eureka dashboard

**Solutions**:

- Ensure Discovery Service is running first
- Check `eureka.client.serviceUrl.defaultZone` in application.properties
- Verify network connectivity to Eureka server
- Check service logs for registration errors
- Wait 30-60 seconds for registration to complete

```bash
# Check Eureka dashboard
curl http://localhost:8761/eureka/apps
```

#### 2. JWT Token Validation Fails

**Symptoms**: 401 Unauthorized errors with valid token

**Solutions**:

- Ensure JWT secret matches in User Service and API Gateway
- Verify token hasn't expired (24-hour default)
- Check token format: `Authorization: Bearer <token>`
- Validate secret is BASE64-encoded and 256+ bits
- Check for whitespace in token

```bash
# Decode JWT token (header and payload only)
echo "eyJhbGc..." | cut -d'.' -f2 | base64 -d
```

#### 3. Database Connection Errors

**Symptoms**: `CommunicationsException` or connection refused

**Solutions**:

- Verify MySQL is running: `mysql -u root -p`
- Check database exists: `SHOW DATABASES;`
- Verify credentials in application.properties
- Check MySQL port (default 3306)
- Ensure `createDatabaseIfNotExist=true` in JDBC URL

```bash
# Test MySQL connection
mysql -h localhost -u bookreview -p -e "SHOW DATABASES;"
```

#### 4. Port Already in Use

**Symptoms**: `Port 8761 is already in use`

**Solutions**:

- Kill process using the port:

  ```bash
  # Windows
  netstat -ano | findstr :8761
  taskkill /PID <PID> /F

  # Linux/Mac
  lsof -ti:8761 | xargs kill -9
  ```

- Change port in application.properties
- Use dynamic ports (`server.port=0`)

#### 5. Inter-Service Communication Fails

**Symptoms**: Review Service can't reach User/Book Service

**Solutions**:

- Verify all services registered with Eureka
- Check `@LoadBalanced` annotation on WebClient.Builder
- Verify service names match in Eureka
- Check network connectivity
- Review service logs for errors

```java
// Correct WebClient configuration
@Bean
@LoadBalanced
public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
}
```

#### 6. BCrypt Password Mismatch

**Symptoms**: Login fails with correct password

**Solutions**:

- Ensure password is hashed before saving
- Verify BCrypt rounds match (10 rounds)
- Check password field length (VARCHAR(255))
- Don't hash password twice
- Use `passwordEncoder.matches()` for verification

### Debug Mode

Enable debug logging for troubleshooting:

```properties
logging.level.root=DEBUG
logging.level.org.springframework.cloud.gateway=TRACE
logging.level.org.springframework.security=TRACE
logging.level.com.bookreviewplatform=TRACE
```

### Health Checks

```bash
# Check all services
curl http://localhost:8761/actuator/health
curl http://localhost:9000/actuator/health

# Check Eureka registry
curl http://localhost:8761/eureka/apps | grep -i status
```

---

## Performance Considerations

### Database Optimization

1. **Indexes**: Ensure proper indexes on frequently queried columns
2. **Connection Pooling**: Configure HikariCP for optimal performance
3. **Query Optimization**: Use EXPLAIN to analyze slow queries
4. **Batch Operations**: Use batch inserts/updates for bulk operations
5. **Lazy Loading**: Configure JPA fetch strategies appropriately

### Caching Strategy (Future)

```java
@Cacheable(value = "books", key = "#id")
public StandardResponse getBookById(UUID id) {
    // Cached for performance
}

@CacheEvict(value = "books", key = "#id")
public StandardResponse updateBook(UUID id, BookRequestDTO dto) {
    // Invalidate cache on update
}
```

### Load Balancing

- Client-side load balancing with Spring Cloud LoadBalancer
- Multiple instances of each service
- Round-robin distribution by default
- Health-based routing

### Horizontal Scaling

```bash
# Start multiple instances
java -jar userservice.jar --server.port=0 &
java -jar userservice.jar --server.port=0 &
java -jar userservice.jar --server.port=0 &

# Eureka automatically load balances
```

---

## Security Best Practices

### Production Checklist

- [ ] Change default JWT secret to secure 256-bit key
- [ ] Use environment variables for sensitive configuration
- [ ] Enable HTTPS/TLS for all communication
- [ ] Implement rate limiting on API Gateway
- [ ] Add security headers (HSTS, CSP, X-Frame-Options)
- [ ] Enable CORS only for trusted origins
- [ ] Implement input validation and sanitization
- [ ] Use prepared statements (JPA handles this)
- [ ] Enable SQL injection protection
- [ ] Implement audit logging for sensitive operations
- [ ] Regular security updates and dependency scanning
- [ ] Implement password complexity requirements
- [ ] Add account lockout after failed login attempts
- [ ] Implement refresh tokens for long-lived sessions
- [ ] Use secrets management (AWS Secrets Manager, HashiCorp Vault)

### Environment Variables

```bash
# .env file (never commit to git)
JWT_SECRET=your-secure-256-bit-secret
DB_USERNAME=bookreview
DB_PASSWORD=secure_password
DB_HOST=localhost
DB_PORT=3306
```

```properties
# application.properties
jwt.secret=${JWT_SECRET}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/users_db
```

---

## Authors

**Lakshan Chamoditha Perera**

---

## Acknowledgments

- **Spring Boot Team**: For the excellent framework and ecosystem
- **Netflix OSS**: For pioneering cloud-native patterns (Eureka, Hystrix)
- **Robert C. Martin**: Clean Code principles and SOLID design
- **Chris Richardson**: Microservices patterns and best practices
- **Martin Fowler**: Software architecture and design patterns
- **Sam Newman**: Building Microservices guidance
- **Eric Evans**: Domain-Driven Design principles

---

## References

### Books

- _Clean Code_ by Robert C. Martin
- _Building Microservices_ by Sam Newman
- _Microservices Patterns_ by Chris Richardson
- _Domain-Driven Design_ by Eric Evans
- _Design Patterns_ by Gang of Four

### Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [JWT.io](https://jwt.io/)
- [BCrypt](https://en.wikipedia.org/wiki/Bcrypt)

### Articles

- [Microservices Architecture](https://microservices.io/)
- [12-Factor App](https://12factor.net/)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

---

## Contact

For questions, issues, or contributions, please:

- Open an issue on GitHub
- Submit a pull request
- Contact the development team

---

## Project Status

**Current Version**: 1.0.0  
**Status**: Active Development  
**Last Updated**: December 22, 2025

### Changelog

#### v1.0.0 (2025-12-22)

- ✅ Initial microservices architecture
- ✅ User authentication with JWT
- ✅ BCrypt password hashing
- ✅ Service discovery with Eureka
- ✅ API Gateway with Spring Cloud Gateway
- ✅ CRUD operations for Users, Books, Reviews
- ✅ Inter-service communication
- ✅ Role-based access control
- ✅ Comprehensive logging
- ✅ Global exception handling
- ✅ Standardized API responses

---

**Built with ❤️ using Spring Boot and Microservices Architecture**
