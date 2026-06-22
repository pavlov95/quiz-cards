# QuizCards

QuizCards is a server-rendered flashcard learning application built with Spring Boot and Thymeleaf. It allows registered users to create decks, add study cards, mark difficult material, and review their cards through a dedicated study flow.

The project was developed as an educational Spring MVC application and demonstrates layered architecture, session-based authentication, form validation, database persistence, authorization checks, and global exception handling.

## Features

- User registration, login, and logout
- Password hashing with Spring Security Crypto
- Session-based authentication
- Protected routes through a session interceptor
- CRUD operations with flashcard decks
- CRUD operaetions with decks
- Mark individual cards as difficult
- Study cards from a selected deck
- Form validation with user-friendly error messages
- Ownership checks for protected deck operations
- Custom error pages for:
  - `403 Forbidden`
  - `404 Not Found`
  - `500 Internal Server Error`
- Responsive Thymeleaf pages styled with custom CSS

## Technologies

- Java 17
- Spring Boot 3.4.0
- Spring MVC
- Spring Data JPA
- Thymeleaf
- Jakarta Bean Validation
- Spring Security Crypto
- MySQL
- Hibernate
- Lombok
- Maven
- HTML5 and CSS3

## Project Architecture

The application follows a layered structure:

The exact package names may vary slightly as the project evolves.

## Main Domain Model

### User

Represents an application account and owns flashcard decks.

### Deck

Groups related cards into a study topic. Deck operations are restricted to the appropriate user where required.

### Card

Contains a question, an answer, an optional source link, and a difficulty flag.

### Role

Represents the role assigned to a user account.

## Prerequisites

Before running the application, install:

- Java Development Kit 17 or newer
- MySQL Server

The project includes the Maven Wrapper, so a separate Maven installation is not required.

## Database Configuration

The application connects to a MySQL database named `quiz_cards`.

Set the database credentials as environment variables before starting the application.

### Windows PowerShell

```powershell
$env:DB_USERNAME="your_mysql_username"
$env:DB_PASSWORD="your_mysql_password"
```

### Windows Command Prompt

```bat
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
```

### Git Bash, Linux, or macOS

```bash
export DB_USERNAME="your_mysql_username"
export DB_PASSWORD="your_mysql_password"
```

Environment variables set in a terminal apply to applications started from that same terminal session.

## Running the Application

Clone the repository and enter the project directory:

```bash
git clone <repository-url>
cd QuizCards
```

Run the application with the Maven Wrapper.

### Git Bash, Linux, or macOS

```bash
./mvnw spring-boot:run
```

### Windows Command Prompt or PowerShell

```bat
mvnw.cmd spring-boot:run
```

After the application starts, open:

```text
http://localhost:8080
```

## Building the Project

Run the test suite:

```bash
./mvnw test
```

Create an executable JAR:

```bash
./mvnw clean package
```

Run the packaged application:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

On Windows, replace `./mvnw` with `mvnw.cmd` where necessary.

## Application Flow

1. Open the landing page.
2. Register a new account or log in.
3. Create a flashcard deck.
4. Add questions and answers to the deck.
5. Optionally add a source link and mark difficult cards.
6. Open the study page and review the cards.
7. Edit or delete cards and decks when needed.
8. Log out when the study session is complete.

## Validation and Error Handling

Form request models are validated with Jakarta Bean Validation. Validation errors are displayed directly in the relevant Thymeleaf forms.

A global `@ControllerAdvice` handles application-specific exceptions, including:

- Invalid login details
- Unavailable usernames and email addresses
- Missing decks
- Unauthorized deck access
- Invalid path variables
- Unexpected server errors

Instead of displaying Spring Boot's default error response, the application renders custom `403`, `404`, and `500` pages.

## Security Notes

- Passwords are stored as hashes rather than plain text.
- Database credentials are read from environment variables.
- `application.properties` must not contain real credentials.
- Deck ownership is checked before protected operations are performed.
- Authentication is managed through the HTTP session and a route interceptor.

This project uses Spring Security Crypto for password hashing, but it does not use the full Spring Security authentication framework.

## Author

Developed by [Dimitar Pavlov](https://github.com/pavlov95) as a SoftUni educational project.
