# Project Management System

This is a simple project management system developed using Spring Boot.

## Setup Instructions

To run this project locally, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/project-management-system.git
   cd project-management-system
   clean install
   mvn spring-boot:run


# API Usage

## Swagger UI

This project uses Swagger UI for API documentation and testing. You can access the Swagger UI by visiting [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) after starting the application.

## Sample Endpoints

Here are some sample endpoints you can use:

- **GET /projects/all:** Retrieve all projects.
- **GET /projects/get/{id}:** Retrieve a project by ID.
- **POST /projects/create:** Create a new project.
- **PUT /projects/update/{id}:** Update an existing project.
- **DELETE /projects/delete/{id}:** Delete a project.

