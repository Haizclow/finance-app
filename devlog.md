# Devlog — Finance Tracker

Progress journal by ticket. Stack: Java 21, Spring Boot 4.1.0, PostgreSQL, JPA/Hibernate.

---

### 2026-08-04 — Ticket 1: Project setup
`feat: initial project setup with PostgreSQL connection`
Initialized the project via Spring Initializr, connected PostgreSQL in Docker, configured application.yaml, first push to GitHub.

### 2026-08-15 — Ticket 2: Transaction CRUD
`feat: add Transaction CRUD REST API`
Transaction entity, TransactionRepository, TransactionService, TransactionController — full GET/POST/PUT/DELETE set.
`fix: correct git email` — git config fix.

### 2026-09-04 — Ticket 3: Category + relation
`feat: add Category CRUD REST API`
Category entity, CRUD for categories, @ManyToOne relation with Transaction.

### 2026-09-10 — Ticket 4: error handling
`feat: add global exception handling`
GlobalExceptionHandler via @ControllerAdvice, ResourceNotFoundException → 404.

### 2026-09-14 — Ticket 5: validation
`feat: add validation`
@Valid, @NotNull, @Positive, @NotBlank on Transaction fields.

### 2026-09-15 — Ticket 6: filtering + id=2 bug
`feat: add filter transactions by type and category`
Derived queries for filtering by ?type= and ?categoryId=.
Along the way, found and fixed a bug: GET by a specific id couldn't find an existing record because of an INNER JOIN on the Category relation for a row with no category (a stale test record created before @NotNull was added) — resolved by cleaning up the DB, no code changes needed.

### 2026-09-17 — Ticket 7: finance summary
`feat: add finance summary endpoint (/api/transaction/summary)`
Aggregate @Query methods with SUM() by transaction type, null handling via BigDecimal.ZERO, SummaryResponse DTO.

### 2026-09-21 — Ticket 8: TransactionRequest DTO
`refactor: use TransactionRequest DTO in create and update endpoints`
create/update now accept TransactionRequest (categoryId instead of a nested Category object), mapped to the Entity via CategoryRepository with handling for a missing category.

### 2026-09-22 — Ticket 9: TransactionResponse DTO
`refactor: introduce TransactionResponse DTO for API responses`
All endpoints now return TransactionResponse (flat categoryId/categoryName instead of a nested Category). Mapping extracted into a private toResponse(), applied via Stream.map() in getAll().

### 2026-09-23 — Ticket 10: pagination and sorting (in progress)
`wip: start pagination in TransactionRepository`
Started working through Pageable/Page<T> in Spring Data JPA to paginate GET /api/transaction while keeping the existing type/categoryId filters. Not finished yet.

### 2026-09-24 — Ticket 10: pagination and sorting (completed)
`feat: add pagination and sorting with combined filters`
Added Pageable/Page<T> support to GET /api/transaction — page/size/sort now work via Spring's built-in PageableHandlerMethodArgumentResolver (no manual @RequestParam needed for page/size/sort). Combined pagination with the existing type/categoryId filters using Page<T>.map() for the Transaction → TransactionResponse conversion. Merged feat into main.

### 2026-09-29 — Ticket 11: unit tests for TransactionService (completed)
`test: add unit tests for TransactionService (getById, getSummary)`
Added JUnit 5 + Mockito tests for TransactionService: @Mock for TransactionRepository/CategoryRepository, @InjectMocks for the service under test. Covered three scenarios: getById returns a correctly mapped TransactionResponse when the record exists, getById throws ResourceNotFoundException when it doesn't, and getSummary returns BigDecimal.ZERO instead of throwing NPE when sumAmountByType returns null (the same null-handling case from ticket 7, now guarded by a test).

### 2026-09-30 — Ticket 12: basic authentication (completed)
`feat: add basic authentication via Spring Security`
Added spring-boot-starter-security. Configured a SecurityConfig class with three beans: PasswordEncoder (BCryptPasswordEncoder), UserDetailsService (a single in-memory user with a BCrypt-hashed password instead of a plaintext one in application.yaml), and SecurityFilterChain (all /api/** paths require authentication via HTTP Basic, everything else is explicitly denied). Verified in Postman: valid credentials on /api/transaction return 200, missing credentials return 401, and an unmatched path returns 403 for a valid user vs 401 for invalid credentials — confirming denyAll() rejects even authenticated users, distinct from requiring authentication.

### 2026-10-01 — Ticket 12 follow-up: deeper review of Spring Security concepts
No code changes. Went back through what beans are, how Dependency Injection works, Basic Auth mechanics (Base64 encoding over the Authorization header), the difference between authentication (401) and authorization (403), and what API means as a general concept. The ticket worked on the first pass, but the underlying mechanics needed a second, slower read to actually sink in.

### 2026-10-02 — Ticket 13: Docker Compose for the full app (completed)
`feat: add Dockerfile and docker-compose for full app deployment`
Added a multi-stage Dockerfile (Maven+JDK build stage, lightweight JRE runtime stage) so the app itself runs as a container instead of from the IDE. Wrote docker-compose.yml with two services, db (postgres) and app, where the app connects to the db by service name (db) rather than localhost inside the Docker network. Externalized the DB password and the Spring Security admin password into a .env file (gitignored) instead of hardcoding them in application.yaml/code. Verified with docker compose up --build: both containers start, Hibernate creates the schema against the fresh containerized Postgres, and Postman confirms Basic Auth and pagination work identically to the manual IDE run.

### 2026-10-02 — Ticket 13 follow-up: .env tracking mishap
No code changes to the app itself. An empty .env file got committed alongside the Dockerfile/docker-compose work, before the .gitignore rule for it was staged — "git add Dockerfile docker-compose.yml .gitignore" picked it up in the same pass. Since .env was empty at that point (no real secrets inside), the leak was harmless; stopped tracking it with git rm --cached, re-verified the fix, and kept the full commit history rather than rewriting it. Lesson: run git status before every commit, don't assume .gitignore protects a file that was staged in the same breath the rule was added.