# finalExam_testing_26843 - Library Management System

This repository contains the backend services and tests for a Library Management System built with Java, Hibernate, and JUnit, using Postgres as the database.

## Features

- **Location Management**: Validates parent-child relationships (e.g., Province -> District).
- **User and Account Management**: User creation and login functionality.
- **Membership**: Different membership tiers (Gold, Silver, Striver) that manage borrowing limits.
- **Borrowing & Returns**: Manages book borrowing limits, return dates, and late fee calculations.
- **Inventory Management**: Room and shelf capacity validation.
- **Reporting**: Location-based aggregation for members and books.

## Tests

There are currently **37** comprehensive JUnit test cases implemented, covering service validations, business logic edge cases, and mocked DAO interactions across:
- `AccountServiceTest`
- `AuthServiceTest`
- `LocationServiceTest`
- `MembershipServiceTest`
- `BorrowingServiceTest`
- `InventoryServiceTest`
- `ReportingServiceTest`

### Running tests
Ensure you have Maven installed. Tests can be run via:
```bash
mvn clean test
```

## Structure

Entities are designed without Lombok annotations and use manually written getters, setters, and constructors as requested.
The project uses Java 21, Hibernate 6.5+, and Mockito (with experimental byte-buddy support for Java 21+).

### Thank you!!

