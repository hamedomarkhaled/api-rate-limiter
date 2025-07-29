# 🔒 Kotlin Spring Boot API Rate Limiter

A configurable and extensible API Rate Limiter built with **Kotlin** and **Spring Boot**.  
It supports multiple algorithms and simulates a distributed environment using multithreading.

---

## 🚀 Features

- ✅ Pluggable architecture with interface-based rate limiter
- ⛽ **Token Bucket** algorithm implemented ✅
- 💧 **Leaky Bucket** algorithm implemented ✅
- 🪟 **Sliding Window** algorithm implemented ✅
- ⚙️ Switchable algorithms via Kotlin configuration (hardcoded config for now)
- 🧪 Unit testing with JUnit (for all algorithms)
- 🔁 Multi-threaded simulation of concurrent clients
- 📈 Logging of allowed and throttled requests

---

## 🧪 How to Test

▶️ Run the App:
```bash
./gradlew bootRun
