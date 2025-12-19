# Ecommerce API - EasyShop – Capstone 3 (Year Up)

## Overview

This project is **Capstone 3**, the **final capstone** for the **Year Up Software Development Program**.

Starter code was provided, including a partially implemented **Spring Boot backend** and a fully functional frontend UI.  
The objective of this capstone was to **complete missing backend functionality**, **fix existing bugs**, and **extend the API** using Java, Spring Boot, and MySQL.

All work for this capstone was completed on the **backend API only**.

---

## Technologies Used

- Java  
- Spring Boot  
- Spring Security (JWT authentication)  
- MySQL  
- JDBC (no JdbcTemplate)  
- Maven  
- Insomnia  
- MySQL Workbench  

---

## Authentication & Authorization

- JWT-based authentication
- Role-based access control:
  - **ADMIN** users can create, update, and delete categories and products
  - **Authenticated users** can access user-specific features
  - **Unauthenticated users** can browse products and categories

---

##  Debugging Highlight: Shopping Cart State Issue

One of the most challenging issues I encountered during this capstone involved the **shopping cart feature**.

###  Problem
When a user added a product to their shopping cart, the item was correctly saved in the database, but **the cart did not update immediately on the frontend**. The item only appeared after refreshing the page.

###  Investigation
To diagnose the issue, I:
- Tested the API endpoints using **Insomnia**
- Verified database inserts and updates using **MySQL Workbench**
- Confirmed that the data persistence layer was working correctly
- Identified that the issue was not with the database, but with the **API response behavior**

###  Solution
I updated the controller logic so that after modifying the shopping cart, the API returns the **updated `ShoppingCart` object** instead of a void response.  
This allowed the frontend to immediately reflect the cart changes without requiring a page refresh.

###  Key Takeaway
This bug reinforced the importance of understanding the **full request–response lifecycle** in backend development. Even when database logic is correct, failing to return the correct response can break the user experience.

---

## Phase 1 – Categories API (Completed)

### Work Completed
- Implemented `CategoriesController`
- Implemented `MySqlCategoryDao` using JDBC
- Restricted create, update, and delete operations to ADMIN users
- Returned correct HTTP status codes (`200`, `201`, `204`, `404`)
- Fully tested using Insomnia

<img width="600" height="1392" alt="image" src="https://github.com/user-attachments/assets/9878b820-3f45-4ab8-847a-f84f462a71a3" />

<img width="600" height="1097" alt="image" src="https://github.com/user-attachments/assets/b2791fb2-fe7d-4df3-b6ba-6052ee9e3d78" />


### Endpoints
- `GET /categories`
- `GET /categories/{id}`
- `POST /categories` (ADMIN)
- `PUT /categories/{id}` (ADMIN)
- `DELETE /categories/{id}` (ADMIN)

---

##  Phase 2 – Bug Fixes (Completed)

### Bug 1 – Product Search Issues
- Fixed incorrect filtering logic for category, price range, and subcategory
- Verified search accuracy using multiple query combinations

<img width="600" height="828" alt="image" src="https://github.com/user-attachments/assets/d5968640-29a0-40aa-b377-59a4ae612323" />


### Bug 2 – Duplicate Products on Update
- Fixed DAO logic where product updates were inserting new rows
- Ensured updates correctly modify existing products
- Updated starter SQL data to reflect proper update behavior
<img width="600" height="838" alt="image" src="https://github.com/user-attachments/assets/f0070a5d-999f-43fa-bdb8-980eb77eb247" />



---

## Phase 3 – Shopping Cart (In Progress)

### Current Status
- Shopping cart feature is actively being implemented
- Cart is tied to the authenticated user
- Uses the existing `shopping_cart` database table
- Persistence across login sessions is being developed

### Planned Endpoints
- `GET /cart`
- `POST /cart/products/{id}`
- `PUT /cart/products/{id}`
- `DELETE /cart`

---

## Testing

- API tested using **Insomnia**
- Database changes verified in **MySQL Workbench**

---

## Conclusion

This capstone demonstrates practical backend development skills using **Java, Spring Boot, JDBC, and MySQL**, including debugging existing systems, implementing secure REST APIs, and extending functionality beyond the starter code.
