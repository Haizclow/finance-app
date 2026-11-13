# finance-app

## Table of Contents
- [About the Project](#about-the-project)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Quick Start & Running](#quick-start--running)
- [Project Structure](#project-structure)

## About the Project
This repository contains the source code for the Finance Tracker application. The program is designed for expense monitoring and financial tracking.

## Technologies
- **Language & Framework**: Java 21, Spring Boot 4 
- **Build**: Maven (via Maven Wrapper ./mvnw)
- **Database**: PostgreSQL 16 
- **Data Access**: Spring Data JPA / Hibernate 
- **Migrations**: Flyway (database schema is created from src/main/resources/db/migration)
- **Security**: Spring Security, HTTP Basic Auth, BCrypt 
- **Boilerplate reduction**: Lombok 
- **Testing**: JUnit + Mockito (unit tests for services)
- **Containerization**: Docker, Docker Compose

## Prerequisites
Before you begin, make sure you have the following installed on your system:
- **Git**
- **Docker** and **Docker Compose** (with Docker Desktop or the Docker daemon running)

## Quick Start & Running

### 1. Clone the repository
Open your terminal and run the commands:  
`git clone https://github.com/Haizclow/finance-app`  
`cd finance-app`
### 2. Configure environment variables (.env)
Create a local `.env` file based on the template:  
`cp .env.example .env`  

Open the created `.env` file in a text editor and provide your actual parameters and access keys.

### 3. Run via Docker
Build and deploy the project with a single command:  
`docker compose up --build`

## Project Structure
```text
finance-app/
├── .github/
├── .idea/
├── .mvn/
├── src/                  # Application source code
├── target/
├── .env.example          # Environment variables template
├── .gitattributes
├── .gitignore            # Files excluded from version control 
├── devlog.md
├── docker-compose.yml    # Services configuration
├── Dockerfile            # Container build script
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md             # Project documentation
```