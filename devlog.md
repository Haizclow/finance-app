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