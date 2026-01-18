# Squares API

A Spring Boot REST API for storing sets of points in a 2D plane and detecting all possible squares.

---

## Features

* Import list of points  
* Add a single point  
* Delete a single point  
* List all points  
* Return all detected squares 
* Count detected squares  

---

## Prerequisites

* Java 17+  
* Maven  

---

## Run instructions

##### Using Eclipse
* File → Import → Maven → Existing Maven Projects  
* Run as Spring Boot Application  

##### Using Command Line
* mvn spring-boot:run   - to run the application
* run the app mvn test - to run automated tests

---

## Endpoints

##### Points
* `POST /points/single` — Add one point  
* `DELETE /points/single` — Delete one point  
* `POST /points` — Import a list of points  
* `GET /points` — Retrieve all points  

##### Squares
* `GET /squares` — Retrieve all detected squares  
* `GET /squares/count` — Retrieve the number of detected squares  

---

## Swagger UI

Swagger UI is available when the application is running:

* http://localhost:8080/swagger-ui/index.html#/


---

## Square Detection Algorithm

* Treats each pair of points as a potential diagonal  
* Computes the other two corners using vector rotation  
* Checks existence in O(1) using a lookup structure  
* Filters out duplicates, rectangles, rhombi, and degenerate cases  
* Supports rotated squares naturally  

---

## Architecture

* **Controllers** — REST endpoints  
* **Services** — Business logic  
* **Repositories** — Persistence (H2 + JPA)  
* **DTOs** — API request/response models  
* **Entities** — Database representation of points  
* **Mappers** — DTO entity conversion  
* **PointKey** — Internal value object for square detection  
* **GlobalExceptionHandler** — Centralized error handling  

---

## Design Decisions

* DTOs prevent exposing JPA entities to the UI  
* Mapper classes centralize DTO entity conversion  
* Diagonal‑based detection algorithm supports rotated squares  
* Light controllers, focused services for maintainability  
* GlobalExceptionHandler keeps services uncluttered
* Input validation for malformed and incorrect format data
* Separate single vs batch endpoints for UI clarity  

---

## Tests

Automated tests cover:

* Square detection logic  
* Point add/delete/import  
* All controller endpoints  
