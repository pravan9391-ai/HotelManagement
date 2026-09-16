# 🏨 Hotel Management System

A full-stack **Hotel Management System** developed using **Angular, Spring Boot, Spring Data JPA, Spring Security, and MySQL**.

The system provides a centralized platform for managing hotel operations including room bookings, food ordering, function hall bookings, vehicle services, driver allocation, payments, and administrative activities.

---

## 🚀 Technologies Used

### Frontend

* Angular
* TypeScript
* HTML5
* CSS3
* Bootstrap
* Angular Router
* Angular HTTP Client

### Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* JWT Authentication
* Maven

### Database

* MySQL

### Development Tools

* IntelliJ IDEA / Eclipse
* Visual Studio Code
* Postman
* Git
* GitHub

---

## ✨ Features

### 👤 User Management

* User registration
* User login
* JWT-based authentication
* Role-based authorization
* User profile management

### 🛏️ Room Management

* View available rooms
* View room details
* Room availability checking
* Room booking
* Booking cancellation
* Booking history

### 🍽️ Food Management

* View food menu
* View food item details
* Order food
* Manage food orders
* Order history

### 🏛️ Function Hall Management

* View available function halls
* View hall details
* Check hall availability
* Book function halls
* Manage hall bookings

### 🚗 Vehicle Management

* View available vehicles
* Vehicle booking
* Vehicle availability management
* Vehicle booking history

### 👨‍✈️ Driver Management

* Driver management
* Driver availability
* Driver allocation
* Driver assignment for vehicle bookings

### 💳 Payment Management

* Payment processing
* Payment status tracking
* Payment history
* Booking-payment association

### 📊 Admin Dashboard

* User management
* Room management
* Booking management
* Food management
* Function hall management
* Vehicle management
* Driver management
* Payment management
* Reports and statistics

---

## 🏗️ System Architecture

```text
                         ┌─────────────────────┐
                         │   Angular Frontend  │
                         │                     │
                         │  Components         │
                         │  Services           │
                         │  Guards             │
                         │  Interceptors       │
                         └──────────┬──────────┘
                                    │
                              HTTP / REST API
                                    │
                                    ↓
                         ┌─────────────────────┐
                         │   Spring Boot API   │
                         │                     │
                         │  Controllers        │
                         │  Services           │
                         │  DTOs               │
                         │  Security + JWT     │
                         │  Exception Handler  │
                         └──────────┬──────────┘
                                    │
                              Spring Data JPA
                                    │
                                    ↓
                         ┌─────────────────────┐
                         │       MySQL         │
                         │                     │
                         │ Users               │
                         │ Rooms               │
                         │ Bookings            │
                         │ Food                │
                         │ Vehicles            │
                         │ Drivers             │
                         │ Payments            │
                         └─────────────────────┘
```

---

## 📁 Project Structure

```text
HotelManagement/
│
├── frontend/
│   └── Angular application
│
├── backend/
│   └── Spring Boot application
│
├── docs/
│   ├── SRS.md
│   ├── HLD.md
│   ├── LLD.md
│   ├── ER-Diagram.png
│   └── API-Documentation.md
│
└── README.md
```

---

## 📂 Backend Structure

```text
backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hotelmanagement/
│   │   │           │
│   │   │           ├── HotelManagementApplication.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── RoomController.java
│   │   │           │   ├── RoomBookingController.java
│   │   │           │   ├── FoodController.java
│   │   │           │   ├── FoodOrderController.java
│   │   │           │   ├── FunctionHallController.java
│   │   │           │   ├── HallBookingController.java
│   │   │           │   ├── VehicleController.java
│   │   │           │   ├── DriverController.java
│   │   │           │   ├── VehicleBookingController.java
│   │   │           │   ├── PaymentController.java
│   │   │           │   └── AdminController.java
│   │   │           │
│   │   │           ├── entity/
│   │   │           │   ├── User.java
│   │   │           │   ├── Role.java
│   │   │           │   ├── Room.java
│   │   │           │   ├── RoomBooking.java
│   │   │           │   ├── FoodItem.java
│   │   │           │   ├── FoodOrder.java
│   │   │           │   ├── FoodOrderItem.java
│   │   │           │   ├── FunctionHall.java
│   │   │           │   ├── HallBooking.java
│   │   │           │   ├── Vehicle.java
│   │   │           │   ├── Driver.java
│   │   │           │   ├── VehicleBooking.java
│   │   │           │   └── Payment.java
│   │   │           │
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           ├── dto/
│   │   │           ├── security/
│   │   │           ├── exception/
│   │   │           └── config/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
└── pom.xml
```

---

## 📂 Frontend Structure

```text
frontend/
│
├── src/
│   ├── app/
│   │   │
│   │   ├── core/
│   │   │   ├── guards/
│   │   │   ├── interceptors/
│   │   │   └── services/
│   │   │
│   │   ├── auth/
│   │   │   ├── login/
│   │   │   └── register/
│   │   │
│   │   ├── rooms/
│   │   │   ├── room-list/
│   │   │   ├── room-details/
│   │   │   └── room-booking/
│   │   │
│   │   ├── food/
│   │   │   ├── food-list/
│   │   │   └── food-order/
│   │   │
│   │   ├── function-hall/
│   │   │   ├── hall-list/
│   │   │   └── hall-booking/
│   │   │
│   │   ├── vehicles/
│   │   │   ├── vehicle-list/
│   │   │   └── vehicle-booking/
│   │   │
│   │   ├── driver/
│   │   ├── payment/
│   │   ├── dashboard/
│   │   └── admin/
│   │
│   ├── assets/
│   └── environments/
│
└── angular.json
```

---

## 🗄️ Database Design

The application uses **MySQL** as its relational database.

### Main Entities

```text
User
Role
Room
RoomBooking

FoodItem
FoodOrder
FoodOrderItem

FunctionHall
HallBooking

Vehicle
Driver
VehicleBooking

Payment
```

### Database Relationship

```text
                         ┌──────────┐
                         │  Users   │
                         └────┬─────┘
                              │
             ┌────────────────┼────────────────┐
             │                │                │
             ↓                ↓                ↓
       RoomBooking        FoodOrder       HallBooking
             │                │                │
             ↓                ↓                ↓
           Room          FoodOrderItem    FunctionHall


                         Users
                           │
                           ↓
                   VehicleBooking
                      /         \
                     ↓           ↓
                 Vehicle       Driver
                     │
                     ↓
                  Payment
```

---

## 🔐 Authentication & Authorization

The application uses **Spring Security and JWT** for authentication.

```text
Angular
   │
   │ Login Request
   ↓
AuthController
   │
   ↓
AuthService
   │
   ↓
Spring Security
   │
   ↓
JWT Token
   │
   ↓
Angular
   │
   │ Authorization: Bearer <JWT>
   ↓
Protected REST APIs
```

The system supports role-based access such as:

```text
ROLE_USER
ROLE_ADMIN
```

---

## 🔗 REST API

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Rooms

```http
GET    /api/rooms
GET    /api/rooms/{id}
POST   /api/rooms
PUT    /api/rooms/{id}
DELETE /api/rooms/{id}
```

### Room Bookings

```http
POST   /api/room-bookings
GET    /api/room-bookings
GET    /api/room-bookings/{id}
PUT    /api/room-bookings/{id}
DELETE /api/room-bookings/{id}
```

### Food

```http
GET    /api/food
GET    /api/food/{id}
POST   /api/food
PUT    /api/food/{id}
DELETE /api/food/{id}
```

### Food Orders

```http
POST /api/food-orders
GET  /api/food-orders
GET  /api/food-orders/{id}
```

### Function Halls

```http
GET  /api/function-halls
POST /api/function-halls
PUT  /api/function-halls/{id}
DELETE /api/function-halls/{id}
```

### Hall Bookings

```http
POST /api/hall-bookings
GET  /api/hall-bookings
GET  /api/hall-bookings/{id}
```

### Vehicles

```http
GET  /api/vehicles
POST /api/vehicles
PUT  /api/vehicles/{id}
DELETE /api/vehicles/{id}
```

### Drivers

```http
GET  /api/drivers
POST /api/drivers
PUT  /api/drivers/{id}
DELETE /api/drivers/{id}
```

### Vehicle Bookings

```http
POST /api/vehicle-bookings
GET  /api/vehicle-bookings
GET  /api/vehicle-bookings/{id}
```

### Payments

```http
POST /api/payments
GET  /api/payments
GET  /api/payments/{id}
```

---

## ⚙️ Backend Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd HotelManagement
```

### 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE hotel_management;
```

### 3. Configure Spring Boot

Update:

```text
backend/src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_management
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

### 4. Run Spring Boot

```bash
cd backend
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

---

## ⚙️ Frontend Setup

Navigate to the Angular application:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Run the application:

```bash
ng serve
```

Frontend:

```text
http://localhost:4200
```

---

## 🔄 Application Flow

### Room Booking

```text
User
 ↓
Angular Room Page
 ↓
Select Room
 ↓
Select Check-in / Check-out
 ↓
Submit Booking
 ↓
Spring Boot REST API
 ↓
RoomBookingController
 ↓
RoomBookingService
 ↓
RoomBookingRepository
 ↓
MySQL
 ↓
Booking Confirmation
 ↓
Angular
```

### Food Ordering

```text
User
 ↓
Food Menu
 ↓
Select Food Items
 ↓
Create Order
 ↓
Spring Boot API
 ↓
FoodOrderService
 ↓
MySQL
 ↓
Order Confirmation
```

### Vehicle + Driver Booking

```text
User
 ↓
Select Vehicle
 ↓
Request Driver
 ↓
Check Driver Availability
 ↓
Vehicle Booking
 ↓
Driver Allocation
 ↓
Payment
 ↓
Booking Confirmation
```

---

## 👨‍💼 Admin Workflow

```text
Admin Login
     ↓
Admin Dashboard
     │
     ├── Manage Users
     ├── Manage Rooms
     ├── Manage Room Bookings
     ├── Manage Food
     ├── Manage Food Orders
     ├── Manage Function Halls
     ├── Manage Hall Bookings
     ├── Manage Vehicles
     ├── Manage Drivers
     ├── Manage Payments
     └── View Reports
```

---

## 🧪 Testing

Backend APIs can be tested using **Postman**.

Testing includes:

* Authentication APIs
* Room APIs
* Booking APIs
* Food APIs
* Function Hall APIs
* Vehicle APIs
* Driver APIs
* Payment APIs
* Admin APIs

---

## 📚 Documentation

Project documentation is maintained inside the `docs/` directory.

```text
docs/
│
├── SRS.md
├── HLD.md
├── LLD.md
├── ER-Diagram.png
└── API-Documentation.md
```

---

## 🔮 Future Enhancements

* Online payment gateway integration
* Email notifications
* SMS notifications
* Invoice generation
* Customer reviews and ratings
* Advanced admin analytics
* Hotel offers and discount management
* Multiple hotel/branch support
* Real-time booking notifications
* Mobile application

---

## 👨‍💻 Development

This project is developed as a **Java Full Stack application** to demonstrate real-world implementation of:

* Angular
* Java
* Spring Boot
* REST APIs
* Spring Security
* JWT
* JPA
* Hibernate
* MySQL
* Git and GitHub

---

## 📄 License

This project is developed for **educational and project purposes**.
