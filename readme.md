# IDP - recceasy.com Identity & Access Management Platform

This repository contains the source code for the recceasy.com Identity and Access Management (IAM) platform. It's a multi-tenant system for onboarding organizations and managing their users.

---
## Project Structure

The project is organized into the following main directories:

-   **/idp**: The core backend service. This is a **Java Spring Boot** application that provides the main business logic and APIs for managing organizations, users, and permissions.
-   **/keycloak**: Contains the configuration (`docker-compose.yml`) for running a local **Keycloak** instance, which serves as our central Identity Provider (IDP).
-   **/idp.recceasy.com**: The frontend web application, built with **Angular**. This provides the user interface for the Super Admin and Organization Admin management consoles.

---
##  TechStack

-   **Backend:** Java 21+, Spring Boot 3
-   **Frontend:** Angular, TypeScript
-   **Authentication:** Keycloak
-   **Database:** PostgreSQL
-   **Containerization:** Docker & Docker Compose

---
## Prerequisites

Before you begin, ensure you have the following installed on your local machine:

-   Java (JDK 21 or later)
-   Apache Maven
-   Node.js and npm (or yarn)
-   Docker and Docker Compose

---
## Getting Started

To get the full application stack running locally, follow these steps in order.

### 1. Start Keycloak & Database

Navigate to the `/keycloak` directory and use Docker Compose to start the services. This will launch a Keycloak container and a PostgreSQL database.

```bash
cd keycloak
docker-compose up -d
