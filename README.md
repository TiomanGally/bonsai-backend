# 🌿 Balkonsai - The Bonsai Backend 🌿

Welcome to **Balkonsai**, the ultimate backend for managing bonsais!  
Why **Balkonsai**? Well, since I don’t have a garden, my bonsais live happily on my **balcony** (*Balkon* in German).  
Yes, I am basically running a **tiny forest on a ledge**.

---

## 🏗️ Architecture - Hexagonalish

This application follows a **lightweight hexagonal architecture**.

### 🔹 Port-Adapter-Architektur

| Port           | Domain Usecase | Adapter         |
|----------------|----------------|-----------------|
| RestController | Domain Usecase | DatabaseService |

### 📂 Folder Structure:

- **`adapter/`** → Handles all communication with the outside world (DB, filesystem, external APIs).
- **`config/`** → Application configuration, security, and setup.
- **`domain/`** → All domain objects and their use cases.
- **`port/`** → Defines the available resources for internal communication.

#### Why not a fully strict Hexagonal Architecture?

- Because **bonsais are small**, and so is this application. 🌱
- Keeping **business logic inside the adapter layer** avoids unnecessary complexity.
- I still get **all the benefits of easy swapping (e.g., replacing the database)** and **clean testing**, while keeping
  things **simple and maintainable**.

---

## 🔐 OAuth2?

OAuth2 in a nutshell:

### 🔹 OAuth2 Flow

| Resource Owner | User-Agent | Client           | Resource Server     | Authentication Server |
|----------------|------------|------------------|---------------------|-----------------------|
| Me             | Browser    | Angular Frontend | Spring Boot Backend | Keycloak              |

Yes, it’s **just like any other OAuth2 setup**—except here, it's overengineered as it will be hosted in my LAN. 🌳🔒

---

## 🚀 Caching - Because APIs Have Limits

Weather API calls are **limited to 1000 per day**.  
To avoid API abuse and unnecessary requests, the backend **caches the weather data**.

### How does it work?

- **Cached results are returned** instead of spamming the API.
- **Cache expires based on `${bonsai.weather.cache.interval}` (in seconds)**.
- **Once expired**, a new request **may** be sent to the API.

So, if my bonsais suddenly **freeze, fry, or drown**, at least **it wasn’t my backend's fault!** ☀️❄️🌧️

---

## Technologies:

- Start a database with: `docker run --name bonsai -e POSTGRES_USER=bonsai -e POSTGRES_PASSWORD=bonsai -e
  POSTGRES_DB=bonsai -p 5432:5432 -d postgres`
- Start a keycloak instance with:
  `docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.3 start-dev`
