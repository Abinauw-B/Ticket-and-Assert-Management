# Postman API Testing Guide for Ticket & Asset Manager

This guide provides detailed instructions on how to connect and test your Spring Boot backend APIs using Postman.

## 🔐 1. Authentication Setup (Basic Auth)

The API is secured using Spring Security with HTTP Basic Authentication. You will need to provide credentials in Postman for almost all endpoints.

**How to configure Basic Auth in Postman:**
1. Open your Request in Postman.
2. Go to the **Authorization** tab.
3. Select **Basic Auth** from the Type dropdown.
4. Enter the Username and Password.

### Available Default Users
*(Sourced from `DataInitializer.java` - seeded on application startup)*

| Role | Username | Password | Notes |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Has full access (can Create/Update/Delete Assets and Tickets). |
| **EMPLOYEE** | `johnsmith` | `admin123` | Can view Assets, but can only manage Tickets. |

---

## 🟢 2. Authentication API

### Get Current Logged-In User
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/auth/me`
- **Auth:** Basic Auth (Any valid user)

---

## 💻 3. Assets API

> **Role Restrictions:** Only **ADMIN** users can Create, Update, or Delete Assets. Regular employees can only view them.

### Get All Assets
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/assets`
- **Auth:** Basic Auth (Admin or Employee)

### Get Asset By ID
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/assets/1`
- **Auth:** Basic Auth (Admin or Employee)

### Create New Asset (Admin Only)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/assets`
- **Auth:** Basic Auth (Admin Only)
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "name": "Dell XPS 15",
  "category": "Laptop",
  "brand": "Dell",
  "model": "XPS 15 9520",
  "serialNumber": "SN-DELL-555123",
  "description": "High-performance developer laptop",
  "status": "AVAILABLE",
  "purchasePrice": 1899.99,
  "vendor": "Dell Direct"
}
```

### Update Asset (Admin Only)
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/assets/1`
- **Auth:** Basic Auth (Admin Only)
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "name": "Dell XPS 15",
  "category": "Laptop",
  "status": "ASSIGNED"
}
```

### Delete Asset (Admin Only)
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/assets/1`
- **Auth:** Basic Auth (Admin Only)

---

## 🎟️ 4. Tickets API

### Get All Tickets
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/tickets`
- **Auth:** Basic Auth (Admin or Employee)

### Get Ticket By ID
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/tickets/1`
- **Auth:** Basic Auth (Admin or Employee)

### Create New Ticket (With Optional Asset Link)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/tickets`
- **Optional Query Param to link Asset:** `http://localhost:8080/api/tickets?assetId=1`
- **Auth:** Basic Auth (Admin or Employee)
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "title": "Cannot connect to VPN",
  "description": "I keep getting disconnected from the company VPN every 5 minutes.",
  "priority": "HIGH",
  "status": "OPEN",
  "category": "Network",
  "subCategory": "VPN Issues",
  "impact": "User level"
}
```

### Update Ticket
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/tickets/1`
- **Optional Query Param to link Asset:** `http://localhost:8080/api/tickets/1?assetId=2`
- **Auth:** Basic Auth (Admin or Employee)
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "title": "Cannot connect to VPN",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "resolutionNotes": "Investigating ISP routing issues."
}
```

### Delete Ticket
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/tickets/1`
- **Auth:** Basic Auth (Admin or Employee)

---

## 💡 Quick Tips for Postman

1. **Variables:** You can set up a Postman Environment with a `{{baseUrl}}` variable set to `http://localhost:8080` to keep your URLs clean (e.g., `{{baseUrl}}/api/assets`).
2. **Pre-request Script:** If you get tired of typing passwords, save your auth at the **Collection level**, and all requests inside will inherit it automatically.
3. **Common Error Codes:**
   - **401 Unauthorized:** You forgot to set Basic Auth, or used the wrong password.
   - **403 Forbidden:** You are logged in as an Employee (`johnsmith`) but tried to perform an Admin-only action (like deleting an Asset).
   - **400 Bad Request:** Your JSON is formatted incorrectly, or you're missing a required field.
