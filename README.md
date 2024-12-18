# Osipalli Microservices Project

## Overview

This project demonstrates a microservices architecture using **Spring Cloud Gateway**, **Spring Authorization Server**, **Nacos Discovery Server**, and **Spring Boot**.

The project includes the following services:
- **osipalli-template-common**: Shared configurations and utilities.
- **osipalli-template-gateway**: Gateway for routing and load balancing.
- **osipalli-template-identity**: Handles OAuth2 token generation.
- **osipalli-template-system**: A protected resource server with secured endpoints.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
    - [Clone the Repository](#1-clone-the-repository)
    - [Start Nacos Discovery Server](#2-start-nacos-discovery-server)
    - [Configure Databases](#3-configure-databases)
    - [Build and Run Microservices](#4-build-and-run-microservices)
    - [Test the Application](#5-test-the-application)
- [Service Configuration](#service-configuration)
- [Endpoints](#endpoints)
- [Contributing](#contributing)
- [License](#licence)

---

## Architecture

### High-Level Diagram

- **osipalli-template-common**: Provides shared security configurations.
- **osipalli-template-gateway**: Routes requests to microservices using Spring Cloud Gateway.
- **osipalli-template-identity**: Provides OAuth2 token authentication.
- **osipalli-template-system**: Exposes a secured resource endpoint.

---

## Prerequisites

Ensure you have the following installed:
- **Java 17+**
- **Maven 3.0+**
- **Nacos Discovery Server** (For service registry)
- **PostgreSQL** (For persistence)

---

## Project Structure

```plaintext
osipalli-microservices/
├── osipalli-template-common/         # Shared configurations and utilities
├── osipalli-template-gateway/        # Gateway service
├── osipalli-tempalte-services
├──────  osipalli-teamplate-services-identity/       # Authentication and token service
├──────  osipalli-template-services-system/         # Resource server
├── README.md                # Documentation
└── pom.xml                  # Parent POM for all modules

```
## Setup Instructions

### 1. Clone the Repository
Start by cloning the project repository:


git clone https://github.com/premkamalosipalli/prem-kamal-osipalli-api
cd -prem-kamal-osipalli-api

### 2. Download and Start Nacos Discovery Server
Download the Nacos server from the official website.
Start Nacos in standalone mode:
bash
Copy code
sh startup.sh -m standalone

### 3. Import Nacos Configuration
Navigate to the nacos directory in the cloned project.
Import the Nacos configuration files into your Nacos server. This will set up service registry configurations needed by the microservices.

### 4. Configure Databases
Will update later

### 5. Build and Run Microservices
Build the entire project using Maven:
bash
Copy code
mvn clean install
Start each microservice

### 6. Test the Application
#### Step 1: Generate a Token
Use the endpoint below to retrieve an OAuth2 token:

Endpoint: http://localhost:8201/osipalli-identity/oauth2/token
Authentication: Basic Auth
Username: <client_id>
Password: <client_secret>
Body:
plaintext
Copy code
grant_type=client_credentials
scope=user.read
Example curl command:

bash
Copy code
curl -X POST \
  http://localhost:8201/osipalli-identity/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&scope=user.read" \
  -u <client_id>:<client_secret>
#### Step 2: Access a Protected Resource
Once you have received the token, use it to access a protected resource:

Endpoint: http://localhost:8201/osipalli-system/v1/user/hello
Authorization: Bearer Token
Authorization Header: Authorization: Bearer <access_token>
Example curl command:

bash
Copy code
curl -X GET \
  http://localhost:8201/osipalli-system/v1/user/hello \
  -H "Authorization: Bearer <access_token>"

---

## Licence

This project was licensed by [Prem Kamal Osipalli](https://premkamalosipalli.com).
