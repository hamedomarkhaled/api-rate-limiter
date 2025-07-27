package com.okhhamed.apiratelimiter.limiter

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random


fun main() {
    val rateLimiterManager : RateLimiterManager = RateLimiterManager()
    val threadPool = Executors.newFixedThreadPool(10)

    val clientIds = listOf("clientA", "clientB", "clientC")

    repeat(50000) { requestNumber ->
        threadPool.submit {
            val clientId = clientIds.random()
            val allowed = rateLimiterManager.allowRequest(clientId)
            val status = if (allowed) "✅ ALLOWED" else "❌ BLOCKED"
            println("[$clientId] Request #$requestNumber => $status")
            Thread.sleep(Random.nextLong(10, 100))
        }
    }

    threadPool.shutdown()
    threadPool.awaitTermination(5, TimeUnit.SECONDS)
}