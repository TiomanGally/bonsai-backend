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

## 🔐 OAuth2 with PKCE (Proof Key for Code Exchange)

OAuth recommends using PKCE on top of Authorization Code Flow as for a single page application the entire source is
available to the browser so we can not securely store a client secret. This means that the Client generates a 'secret'
called 'Code Verifier' and with this Code Verifier it generates a transform value called 'Code Challenge'. To retrieve
the authorization code by calling the '/auth' endpoint it sends the Code Challenge. This will be stored on the auth
server side. The Auth Server responds with a code which can then be used to call the '/token' endpoint. But this time
with
the actual secret, the Code Verifier. The Auth server verifies that the Code Challenge is based on the Code Verifier and
responds with an access_token.
For more information, check [RFC7636](https://datatracker.ietf.org/doc/html/rfc7636)

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

## Getting Started:

As we want to have container which will be easier to use on my homelab server we need to create a network so the
container
can communication between each other

```shell
docker network create bonsai-playground
```

As an authorization server I use keycloak. Run it with the following command:

```shell
docker run --name bonsai-keycloak --network bonsai-playground \
-p 8080:8080 \
-e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
-e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.3 start-dev
```

To start the postgres database first execute following:

```shell
docker run --name bonsai-postgres-container \
--network bonsai-playground \
--env POSTGRES_USER=bonsai \
--env POSTGRES_PASSWORD=bonsai \
--env POSTGRES_DB=bonsai \
--publish 5432:5432 \
--detach postgres
```

To start the App you need to build it first:

```shell
./gradlew build && docker build -t bonsai-backend:1.0 .
```

and then run it:

```shell
docker run --volume ~/Documents/bonsai:/bonsai --network bonsai-playground \
--name bonsai-backend \
--env SPRING_PROFILES_ACTIVE=prod \
--publish 8123:8123 \
--detach bonsai-backend:1.0
```
