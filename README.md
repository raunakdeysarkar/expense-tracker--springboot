Expense Tracker

A simple expense tracking application built with Java and Spring Boot.

This project started as a Core Java console-based Expense Tracker and was later extended into a web application using Spring Boot, REST APIs, Spring Data JPA, and MySQL.

Technologies Used
Java
Spring Boot
Spring Data JPA
Hibernate
MySQL
Maven
HTML
CSS
JavaScript
Features
Add an expense
View all expenses
View an expense by ID
Update an expense
Delete an expense
Calculate total expenses
Filter expenses by category
Calculate monthly expenses
Basic input validation
Black and green web interface
Project Architecture

The application follows a simple layered architecture.

Frontend → Controller → Service → Repository → MySQL

Controller

Handles HTTP requests and provides the REST API endpoints.

Service

Contains the business logic, such as calculating totals and filtering expenses.

Repository

Uses Spring Data JPA to communicate with the database.

MySQL

Stores the expense data.

API Endpoints
Method	Endpoint	Description
POST	/expenses	Add an expense
GET	/expenses	Get all expenses
GET	/expenses/{id}	Get an expense by ID
PUT	/expenses/{id}	Update an expense
DELETE	/expenses/{id}	Delete an expense
GET	/expenses/summary	Get total expenses
GET	/expenses/category/{category}	Get expenses by category
GET	/expenses/month/{month}	Get total expenses for a month
Frontend

The frontend uses basic HTML, CSS, and JavaScript.

JavaScript communicates with the Spring Boot REST API using HTTP requests.

The frontend is intentionally kept simple because the main focus of this project is the Java and Spring Boot backend.

Running the Application
1. Clone the repository
git clone YOUR_GITHUB_REPOSITORY_URL
2. Configure MySQL

Create a MySQL database and update the database settings in:

src/main/resources/application.properties

Do not commit your real database password to GitHub.

3. Run the Application

On Windows:

.\mvnw.cmd spring-boot:run

The application will run at:

http://localhost:8080

Project Background

This project started as a Core Java Expense Tracker.

The original version used Java collections and file-based storage.

It was then extended using Spring Boot by:

Creating REST APIs
Adding a Service layer
Adding a Repository layer
Replacing file-based persistence with MySQL
Using Spring Data JPA for database operations
Connecting a simple web frontend to the REST API

The goal of this project was to understand how a basic Java application can be extended into a web-based Spring Boot application.

That's it.

The only things that will appear as special formatting are the two small command blocks under Running the Application. The architecture is deliberately just one line:

Frontend → Controller → Service → Repository → MySQL

So there's no confusing nested Markdown diagram anymore.
