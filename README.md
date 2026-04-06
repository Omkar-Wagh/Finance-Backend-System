# Finance Data Processing Backend

## Overview
This project is a backend system for managing financial records with role-based access control. It supports user management, record operations, and dashboard analytics.

---

## Tech Stack
- Java (Spring Boot)
- Spring Security (JWT Authentication)
- JPA / Hibernate
- PostgreSQL
- MapStruct

---
## Setup Instructions

1. Clone the repository
2. Configure application.properties
3. Run the application

### Database Configuration

Update your PostgreSQL credentials in:
src/main/resources/application.properties

## Features

### User Management
- Admin creates users
- Role-based access (Viewer, Analyst, Admin)
- JWT authentication

### Financial Records
- Create, update, delete (soft delete)
- Assign ownership
- Pagination support

### Search
- Dynamic filtering (type, category, amount)
- Role-based restrictions

### Dashboard
- Total income, expense, net balance
- Category-wise insights
- Global analytics (Admin only)

---

## Role-Based Access

| Role | Access |
|------|--------|
| Viewer | Dashboard only |
| Analyst | Dashboard + Search |
| Admin | Full system access |

---

## API Endpoints

### User APIs
- POST `/api/user/register`
- POST `/api/user/login`
- PUT `/api/user/update`

### Record APIs
- GET `/api/record/get-records`
- POST `/api/record/create-record`
- PUT `/api/record/update-record`
- PUT `/api/record/delete-record/{id}`

### Dashboard APIs
- GET `/api/record/dashboard-data/{userId}`
- GET `/api/record/dashboard-global-data`

### Search API
- GET `/api/record/records/search`

Example: /records/search?type=EXPENSE&minAmount=100


---

## Security
- JWT-based authentication
- Role-based authorization using Spring Security

---

## Assumptions
- Users are created by Admin (no public registration)
- Soft delete is used instead of hard delete
- Date-based trends can be extended

---

## Future Improvements
- Rate limiting
- Unit testing
---

## Conclusion
This project demonstrates backend design, access control, and data processing capabilities with clean architecture and scalable design principles.