# StockPro - Inventory Management System (Backend REST API)

Robust and scalable enterprise-grade backend built using **Spring Boot** and **Java**, fully integrated with a managed **MySQL** database instance. The production server is deployed live on cloud containers via Railway.

## 🔗 Production API URL
* **Live Base URL:** `https://stockpro-backend-production-b160.up.railway.app`

## 🛡️ Key System Architecture
* **Framework:** Spring Boot (Java)
* **Database & ORM:** MySQL & Spring Data JPA
* **Security:** Context-based authentication mechanics
* **Cloud Platform:** Railway App (with Persistent Volume attachment for MySQL storage)

## ⚡ Main REST Endpoints
* `POST /api/auth/login` - User Authentication
* `GET /api/products` - Live Inventory Retrieval
* `POST /api/suppliers` - Vendor Registration
