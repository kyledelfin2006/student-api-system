# Student-API-System

A RESTful HTTP API for managing student records, built with Java and the Spark framework. The system supports full CRUD operations, GWA-based filtering, keyword search, JSON file persistence, and input validation.

---
![Java](https://img.shields.io/badge/Java-21+-orange)
![SparkJava](https://img.shields.io/badge/SparkJava-2.9.4-red)
![Jackson](https://img.shields.io/badge/Jackson-2.x-blue)
![Status](https://img.shields.io/badge/status-working-brightgreen)

## Project Structure

```
src/
└── api/
    ├── controller/
    │   └── StudentAPIController.java
    ├── manager/
    │   └── StudentManager.java
    ├── repository/
    │   ├── Repository.java
    │   └── StudentRepository.java
    ├── storage/
    │   ├── Storage.java
    │   └── StudentStorage.java
    ├── models/
    │   └── Student.java
    ├── dto/
    │   └── StudentInput.java
    ├── responses/
    │   ├── ApiResponse.java
    │   └── ErrorResponse.java
    ├── util/
    │   ├── StudentIDGenerator.java
    │   └── Validator.java
    └── exceptions/
        └── InvalidInputException.java
```

---

## Architecture Overview

The application follows a layered architecture pattern where each layer has a single, well-defined responsibility. Dependencies flow in one direction: from the outer HTTP layer inward toward data and storage.

```
HTTP Request
     |
     v
[StudentAPIController]   -- Route definitions, HTTP request/response handling
     |
     v
[StudentManager]         -- Business logic, orchestration, in-memory index
     |
    / \
   v   v
[StudentRepository]    [StudentStorage]
(in-memory list)       (JSON file on disk)
```

The `StudentManager` maintains two data structures simultaneously:

- `StudentRepository` — an `ArrayList`-backed in-memory list used as the authoritative data source.
- `HashMap<String, Student> idIndex` — a secondary index keyed by student ID, used for O(1) lookups on `GET /api/students/:id`, `DELETE`, and `PATCH`.

On startup, the manager loads data from the JSON file into the repository and rebuilds the index. Every write operation (`POST`, `DELETE`, `PATCH`) persists the full repository back to disk.

---

## Package Breakdown

### Controller

**`StudentAPIController`**

The application entry point. Initializes all dependencies, configures the Spark HTTP server on port `8081`, and defines all route handlers. Each route delegates to `StudentManager` and serializes the result with Jackson's `ObjectMapper`.

Responsibilities:
- Dependency wiring (manual constructor injection — no IoC container)
- Route registration
- HTTP status code assignment
- JSON serialization of responses and errors

### Manager

**`StudentManager`**

The core business logic layer. It owns the in-memory state and coordinates between the repository and storage.

Key behaviors:

- `loadFromStorage()` — called once at construction. Reads the JSON file, clears the repository, computes the maximum existing numeric ID to resume auto-increment correctly, and reloads all students.
- `saveToStorage()` — called after every mutation. Serializes the full repository list to the JSON file.
- `resetIndex()` — rebuilds the `HashMap` from the current repository contents. Called after load and available for full rebuilds if needed.
- `isEmailUnique(String email)` — streams the repository to check for email collisions before `POST` and `PATCH`.
- `patchStudent()` — applies partial updates. Each field is only updated when the corresponding value in `StudentInput` is non-null, preserving existing data for omitted fields.

GWA filtering semantics (lower GWA is better in the Philippine grading system):

| Method | Parameter | Behavior |
|---|---|---|
| `filterMaxGwa(double)` | `maxGwa` | Returns students with `gwa <= maxGwa` (students at or above the grade threshold) |
| `filterMinGwa(double)` | `minGwa` | Returns students with `gwa >= minGwa` (students at or below the grade threshold) |
| `filterMinMaxGwa(double, double)` | `minGwa`, `maxGwa` | Returns students within the inclusive GWA range |

### Repository

**`Repository<T>` (interface)**

```java
public interface Repository<T> {
    void add(T type);
    void remove(T type);
    List<T> getAll();
    void clear();
}
```

A generic interface defining the contract for any in-memory collection. Decouples the manager from a concrete list implementation.

**`StudentRepository`**

Implements `Repository<Student>` using an `ArrayList`. The `getAll()` method returns a `Collections.unmodifiableList` view, preventing external mutation of the internal list. Extends the interface with `addAll(List<Student>)` for bulk loading from storage.

### Storage

**`Storage<T>` (interface)**

```java
public interface Storage<T> {
    void save(List<T> items) throws IOException;
    List<T> load() throws IOException;
}
```

A generic persistence interface. Separates the concept of storage from any specific medium (file, database, etc.).

**`StudentStorage`**

Implements `Storage<Student>` using Jackson's `ObjectMapper` for JSON serialization and deserialization. The target file path is injected via the constructor (`"Students.json"` by default). If the file does not exist on `load()`, an empty list is returned rather than throwing.

### Models

**`Student`**

The core domain object. Fields:

| Field | Type | Notes |
|---|---|---|
| `id` | `String` | Zero-padded 4-digit integer, e.g. `"0001"` |
| `firstName` | `String` | Validated on construction and update |
| `lastName` | `String` | Validated on construction and update |
| `course` | `String` | Validated on construction and update |
| `yearLevel` | `int` | Integer 1–6 |
| `gwa` | `double` | Range 1.0–5.0 |
| `email` | `String` | Normalized to lowercase on save |

The constructor calls `StudentIDGenerator.generateNextID()` and runs all fields through `Validator` before assignment. The `@JsonPropertyOrder` annotation controls serialized field order in JSON output.

A no-arg constructor is also present for Jackson deserialization when loading from the JSON file.

### DTO

**`StudentInput`**

A plain data-transfer object used to deserialize request bodies for `POST` and `PATCH`. All fields use boxed types (`Integer`, `Double`, `String`) so that `null` can represent an absent field in partial update scenarios. No validation logic lives here.

### Responses

**`ApiResponse<T>`**

A generic success envelope:

```json
{
  "success": true,
  "message": "Student Added Successfully",
  "data": { ... },
  "timestamp": 1715000000000
}
```

Three constructors cover: message-only, data-only, and message-with-data combinations.

**`ErrorResponse`**

A structured error envelope:

```json
{
  "error": "Validation failed",
  "details": "GWA must be between 1.0 and 5.0!",
  "statusCode": 400,
  "timestamp": 1715000000000
}
```

Two constructors: one with just an error and status code, one that additionally accepts a `details` string for validation messages.

### Utilities

**`StudentIDGenerator`**

Manages a process-wide auto-incrementing ID using `AtomicInteger` for thread safety. IDs are formatted as zero-padded 4-digit strings (`"0000"` through `"9999"`). On startup, `setNextId()` is called with `(maxExistingId + 1)` to prevent ID collisions after restarts.

**`Validator`**

A stateless utility class containing static validation methods. Each method either returns the validated (and possibly normalized) value, or throws `InvalidInputException`.

Constants:

```java
MIN_GWA  = 1.0
MAX_GWA  = 5.0
MIN_YEARS = 1
MAX_YEARS = 6
```

### Exceptions

**`InvalidInputException`**

Extends `RuntimeException`. Used uniformly throughout the `Validator` and `StudentManager` to signal constraint violations. Caught in the controller and mapped to HTTP 400 responses.

---

## Data Flow

### POST /api/students/

```
Request Body (JSON)
  -> ObjectMapper.readValue -> StudentInput
  -> StudentManager.createStudent()
      -> isEmailUnique() check
      -> new Student(input fields)  // triggers Validator + IDGenerator
      -> repository.add()
      -> storage.save()
      -> idIndex.put()
  -> ApiResponse<Student> (201)
```

### GET /api/students/:id

```
Path param :id
  -> StudentManager.findStudent(id)
      -> idIndex.get(id)  // O(1) HashMap lookup
  -> Student or null
  -> Student (200) or ErrorResponse (404)
```

### PATCH /api/students/:id

```
Path param :id + Request Body (JSON)
  -> ObjectMapper.readValue -> StudentInput
  -> StudentManager.patchStudent(id, updates)
      -> idIndex.get(id)
      -> for each non-null field in updates:
           -> Validator.validate*() -> existing.set*()
      -> email uniqueness check (only if email changed)
      -> storage.save()
  -> ApiResponse<Student> (200) or ErrorResponse (404/400)
```

### DELETE /api/students/:id

```
Path param :id
  -> StudentManager.deleteStudent(id)
      -> idIndex.get(id)
      -> repository.remove()
      -> storage.save()
      -> idIndex.remove()
  -> ApiResponse (200) or ErrorResponse (404)
```

---

## API Endpoints

Base URL: `http://localhost:8081`

| Method | Path | Description | Success Status |
|---|---|---|---|
| `GET` | `/api/health` | Health check | 200 |
| `GET` | `/api/students` | Retrieve all students | 200 |
| `GET` | `/api/students/:id` | Retrieve student by ID | 200 |
| `POST` | `/api/students/` | Create a new student | 201 |
| `DELETE` | `/api/students/:id` | Delete student by ID | 200 |
| `PATCH` | `/api/students/:id` | Partially update a student | 200 |
| `GET` | `/api/students/search?q=` | Search by name or email | 200 |
| `GET` | `/api/students/filter` | Filter by GWA range | 200 |

### Query Parameters — Filter

| Parameter | Type | Description |
|---|---|---|
| `minGwa` | `double` | Minimum GWA (inclusive). Returns students at this grade or worse. |
| `maxGwa` | `double` | Maximum GWA (inclusive). Returns students at this grade or better. |

Both parameters may be combined to define an inclusive range.

### Request Body — POST and PATCH

```json
{
  "firstName": "Juan",
  "lastName": "dela Cruz",
  "course": "Computer Science",
  "yearLevel": 2,
  "gwa": 1.75,
  "email": "juan@example.com"
}
```

For `PATCH`, all fields are optional. Only provided fields are updated.

---

## Validation Rules

| Field | Rules |
|---|---|
| `firstName` / `lastName` | Required. Length 2–100. Letters only, single spaces or hyphens between words. |
| `course` | Required. Length 2–50. Letters only, single spaces or hyphens between words. |
| `yearLevel` | Required. Integer between 1 and 6 inclusive. |
| `gwa` | Required. Double between 1.0 and 5.0 inclusive. |
| `email` | Required. Must match standard email format. Stored in lowercase. Must be unique across all students. |

Validation failures produce a `400` response with an `ErrorResponse` containing a `details` field describing the constraint that was violated.

---

## ID Generation

Student IDs are zero-padded 4-digit strings assigned sequentially:

```
"0000", "0001", "0002", ...
```

The generator uses `AtomicInteger` to ensure safe concurrent access. On application startup, `StudentManager` scans all loaded student IDs, extracts the maximum numeric value, and calls `StudentIDGenerator.setNextId(maxId + 1)` to resume from the correct position. This prevents duplicate IDs across restarts.

---

## Persistence

Student data is persisted to a JSON file (`Students.json`) in the working directory. The file is read once on startup and written in full after every create, update, or delete operation (write-through strategy). There is no partial update to the file — the entire list is serialized on each save.

Example file structure:

```json
[
  {
    "id": "0001",
    "firstName": "Juan",
    "lastName": "dela Cruz",
    "course": "Computer Science",
    "yearLevel": 2,
    "gwa": 1.75,
    "email": "juan@example.com"
  }
]
```

If the file does not exist on startup, the application initializes with an empty repository and creates the file on the first write.

---

## Error Handling

| Scenario | HTTP Status | Response Type |
|---|---|---|
| Student not found | 404 | `ErrorResponse` |
| Validation constraint violated | 400 | `ErrorResponse` with `details` |
| Malformed JSON body | 400 | `ErrorResponse` with exception message |
| Duplicate email | 400 | `ErrorResponse` with `details` |
| Unmatched route | 404 | `ErrorResponse` — caught by Spark's `notFound` handler |

`InvalidInputException` (a `RuntimeException`) is thrown by `Validator` and `StudentManager` and caught explicitly in each controller route that performs writes. Generic `Exception` is caught as a fallback for JSON parse failures.

---

## Running the Application

**Prerequisites:**
- Java 11 or higher
- Maven or Gradle (depending on your build configuration)
- Dependencies: Spark Java (`spark-core`), Jackson Databind

**Start the server:**

```bash
mvn compile exec:java -Dexec.mainClass="api.controller.StudentAPIController"
```

The server starts on port `8081`. On startup, the console prints a summary of all registered endpoints.

**Example request:**

```bash
# Create a student
curl -X POST http://localhost:8081/api/students/ \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Juan","lastName":"dela Cruz","course":"Computer Science","yearLevel":2,"gwa":1.75,"email":"juan@example.com"}'

# Get all students
curl http://localhost:8081/api/students

# Filter students with GWA between 1.5 and 2.5
curl "http://localhost:8081/api/students/filter?minGwa=1.5&maxGwa=2.5"

# Search by name
curl "http://localhost:8081/api/students/search?q=juan"
```
