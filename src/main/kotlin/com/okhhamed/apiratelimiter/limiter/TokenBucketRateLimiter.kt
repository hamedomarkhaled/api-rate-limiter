package com.okhhamed.apiratelimiter.limiter

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class TokenBucketRateLimiter(private val capacity: Int, private val refillRatePerSecond: Int): RateLimiter {
    private val logger = LoggerFactory.getLogger(TokenBucketRateLimiter::class.java)

    private data class Bucket(var tokens: Double, var lastRefillTimestamp: Long)

    private val buckets:  MutableMap<String, Bucket> = ConcurrentHashMap()

    override fun allowRequest(clientId: String): Boolean {
        val now = System.nanoTime()
        val bucket = buckets.computeIfAbsent(clientId) {
            Bucket(capacity.toDouble(), now)
        }

        synchronized(bucket) {
            refill(bucket, now)
            return if (bucket.tokens >= 1) {
                bucket.tokens -=1
                logger.info("✅ Request allowed for client $clientId | Tokens left: ${bucket.tokens}")
                true
            } else {
                logger.warn("❌ Request denied for client $clientId | Tokens left: ${bucket.tokens}")
                false
            }
        }
    }

    private fun refill(bucket: Bucket, now: Long) {
        val elapsedTimeInSeconds = (now - bucket.lastRefillTimestamp).toDouble() / 1_000_000
        val tokensToAdd = elapsedTimeInSeconds * refillRatePerSecond
        if(tokensToAdd > 0) {
            bucket.tokens = minOf(capacity.toDouble(), bucket.tokens + tokensToAdd)
            bucket.lastRefillTimestamp = now
        }
    }


}