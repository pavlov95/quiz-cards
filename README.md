QuizCards

QuizCards is a server-rendered flashcard learning application built with Spring Boot and Thymeleaf. Registered users can create decks, add study cards, mark difficult material, and review their cards through a dedicated study flow.

The project was developed as an educational Spring MVC application. It demonstrates layered architecture, session-based authentication, form validation, relational database persistence, authorization checks, role-based moderation, automatic demo-data generation, and global exception handling.

Features
User registration, login, and logout
Password hashing with Spring Security Crypto
Session-based authentication
Protected routes through a session interceptor
CRUD operations on decks
CRUD operations on cards
Public and private deck visibility
Mark individual cards as difficult
Study cards from a selected deck
Optional source links for cards
Form validation with user-friendly error messages
Ownership checks for protected deck and card operations
Administrator moderation of public decks
Automatic demo-user and sample-data generation
Custom error pages for:
403 Forbidden
404 Not Found
500 Internal Server Error
Thymeleaf pages styled with custom CSS
User Roles
User

A regular user can:

Create decks
Choose whether a deck is public or private
Edit and delete their own decks
Add, edit, and delete cards in their own decks
View and study public decks belonging to other users
View and study their own private decks
Administrator

An administrator can perform the same actions as a regular user and can also delete public decks belonging to other users.

Administrators cannot edit another user’s deck or cards, and they cannot delete another user’s private deck.

The public registration form always creates regular USER accounts. The ADMIN role is not assignable through the registration form.

Demo Data

When the application starts, it can automatically create a populated demonstration account.

Demo account
Username: demo
Password: Demo123!
Email: demo@example.com
Role: ADMIN

The demo account owns:

10 public decks
2 private decks
Multiple cards inside every deck

The sample deck and card definitions are stored in:

src/main/resources/demo/demo-data.json

The demo-data seeder reads this JSON file and persists the user, decks, and cards when the demo account does not already exist.

Automatic demo-data generation can be disabled with:

DEMO_DATA_ENABLED=false

The corresponding application property is:

app.demo-data.enabled=${DEMO_DATA_ENABLED:true}
Technologies
Java 17
Spring Boot 3.4.0
Spring MVC
Spring Data JPA
Thymeleaf
Jakarta Bean Validation
Spring Security Crypto
MySQL
Hibernate
Jackson
Lombok
Maven
JUnit 5
Mockito
MockMvc
HTML5
CSS3
Project Architecture

The application follows a layered structure:

src/main/java/com/quizCards/demo/
├── config/          Application configuration and demo-data seeding
├── entities/        JPA entities
├── exceptions/      Custom application exceptions
├── repositories/    Spring Data JPA repositories
├── security/        Session interceptor
├── services/        Business logic and authorization
└── web/
├── controllers  Spring MVC controllers
└── dto/         Form request models

Application resources are organized under:

src/main/resources/
├── demo/            JSON demo-data definitions
├── static/css/      Page stylesheets
├── templates/       Thymeleaf templates
└── application.properties
Main Domain Model
User

Represents an application account. A user has a username, email address, encoded password, role, and owned decks.

Deck

Groups related cards into a study topic. A deck belongs to one user and can be public or private.

Card

Contains a question, answer, optional source link, and difficulty flag. Every card belongs to a deck.

Role

Represents the authorization level assigned to a user:

USER
ADMIN
Prerequisites

Before running the application, install:

Java Development Kit 17 or newer
MySQL Server

The project includes the Maven Wrapper, so a separate Maven installation is not required.

Database Configuration

The application connects to a MySQL database named quiz_cards.

Set the database credentials as environment variables before starting the application.

Windows PowerShell
$env:DB_USERNAME="your_mysql_username"
$env:DB_PASSWORD="your_mysql_password"
Windows Command Prompt
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
Git Bash, Linux, or macOS
export DB_USERNAME="your_mysql_username"
export DB_PASSWORD="your_mysql_password"

Environment variables set in a terminal apply to applications started from that same terminal session.


After the application starts, open:

http://localhost:8080

The application creates the configured MySQL database if it does not already exist.

Run the packaged application:

Application Flow
Open the landing page.
Register a new account or log in with the demo account.
Open an existing public deck or create a new deck.
Choose whether the deck should be public or private.
Add questions and answers to the deck.
Optionally add source links and mark difficult cards.
Open the study page and review the cards.
Edit or delete owned cards and decks when needed.
Log out when the study session is complete.
Validation and Error Handling

Form request models are validated with Jakarta Bean Validation. Validation errors are displayed directly in the relevant Thymeleaf forms.

A global @ControllerAdvice handles application-specific exceptions, including:

Invalid login details
Unavailable usernames
Unavailable email addresses
Missing decks
Unauthorized deck access
Invalid path variables
Unexpected server errors

Instead of displaying Spring Boot’s default Whitelabel error page, the application renders custom 403, 404, and 500 pages.

Authorization Rules

Authorization is enforced in the service layer.

Only a deck owner can edit a deck.
Only a deck owner can add, edit, or delete its cards.
Only a deck owner can delete a private deck.
A regular user cannot delete another user’s deck.
An administrator can delete another user’s public deck.
Hiding buttons in Thymeleaf is used for presentation only; the service layer performs the actual permission checks.
Security Notes
Passwords are stored as BCrypt hashes rather than plain text.
Database credentials are read from environment variables.
Real database credentials should never be committed.
Public registration cannot assign administrator privileges.
Authentication is managed through the HTTP session and a route interceptor.
Ownership and role checks are performed before protected operations.
Demo credentials are intended only for local project evaluation.

This project uses Spring Security Crypto for password hashing, but it does not use the complete Spring Security authentication framework.

Future Improvements

Possible future additions include:

Email verification during registration
Password reset by email
Administrator dashboard
User-account moderation
Search and filtering for public decks
Deck categories and tags
Study statistics and progress tracking
Pagination
Full Spring Security integration
Deployment configuration
Author

Developed by Dimitar Pavlov as a SoftUni educational project.