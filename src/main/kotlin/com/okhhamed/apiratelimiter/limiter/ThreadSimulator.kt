package com.okhhamed.apiratelimiter.limiter

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun main() {
    val clientIds = listOf("clientA", "clientB", "clientC")
    val totalRequests = 500
    val threadCount = 10

    val algorithms = listOf(
        "TokenBucket" to TokenBucketRateLimiter(
            capacity = 3,
            refillRatePerSecond = 1
        ),
        "LeakyBucket" to LeakyBucketRateLimiter(
            capacity = 10,
            leakRatePerSecond = 2
        ),
        "SlidingWindow" to SlidingWindowRateLimiter(
            maxRequests = 5,
            windowInMills = 3000
        )
    )

    for ((name, limiter) in algorithms) {
        println("\n=== Running simulation for $name ===")

        val statsMap = clientIds.associateWith { Stats() }.toMutableMap()
        val threadPool = Executors.newFixedThreadPool(threadCount)

        repeat(totalRequests) { requestNumber ->
            threadPool.submit {
                val clientId = clientIds.random()
                val allowed = limiter.allowRequest(clientId)
                val stats = statsMap[clientId]!!

                if (allowed) {
                    stats.allowed++
                } else {
                    stats.blocked++
                }

                val status = if (allowed) "✅ ALLOWED" else "❌ BLOCKED"
//                println("[$name][$clientId] Request #$requestNumber => $status")

                Thread.sleep(Random.nextLong(10, 100))
            }
        }

        threadPool.shutdown()
        threadPool.awaitTermination(10, TimeUnit.SECONDS)

        val totalAllowed = statsMap.values.sumOf { it.allowed }
        val totalBlocked = statsMap.values.sumOf { it.blocked }
        println("\n--- $name Summary ---")
        println("Total Allowed: ✅ $totalAllowed")
        println("Total Blocked: ❌ $totalBlocked\n")
    }
}

data class Stats(var allowed: Int = 0, var blocked: Int = 0)
