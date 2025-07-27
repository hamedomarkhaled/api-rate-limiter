# 🔒 Kotlin Spring Boot API Rate Limiter

A configurable and extensible API Rate Limiter built with **Kotlin** and **Spring Boot**.  
It supports multiple algorithms and simulates a distributed environment using multithreading.

---

## 🚀 Features

- ✅ Pluggable architecture with interface-based rate limiter
- ⛽ **Token Bucket** algorithm implemented  ✅
- ⚙️ Switchable algorithms via Kotlin configuration
- 🧪 Unit testing with JUnit
- 🔁 Multi-threaded simulation of concurrent clients
- 📈 Logging of allowed and throttled requests
---



## 🧪 How to Test
▶️ Run the App
```
./gradlew bootRun
```

Then hit the rate-limited endpoint:

```
curl "http://localhost:8080/api/request?clientId=client1"
```

# 🔁 Run Multi-threaded Simulation
Run `ThreadSimulator.kt` from your IDE to simulate distributed clients hitting the rate limiter.

## ✅ Unit Tests
Run unit tests using:

```
./gradlew test
```
