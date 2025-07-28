package com.okhhamed.apiratelimiter.limiter

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min
import org.slf4j.LoggerFactory

class TokenBucketRateLimiter(
    private val capacity: Int,
    private val refillRatePerSecond: Int
) : RateLimiter {
    private val logger = LoggerFactory.getLogger(TokenBucketRateLimiter::class.java)

    private data class Bucket(
        var tokens: Int,
        var lastRefillTimeMillis: Long
    )

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun allowRequest(clientId: String): Boolean {
        val now = System.currentTimeMillis()
        val bucket = buckets.computeIfAbsent(clientId) {
            Bucket(capacity, now)
        }

        refill(bucket, now)

        return if (bucket.tokens > 0) {
            bucket.tokens--
            logger.info("✅ Request allowed for client $clientId | Tokens left: ${bucket.tokens}")
            true
        } else {
            logger.warn("❌ Request denied for client $clientId | Tokens left: ${bucket.tokens}")
            false
        }
    }

    private fun refill(bucket: Bucket, now: Long) {
        val elapsedSeconds = (now - bucket.lastRefillTimeMillis) / 1_000_000
        if (elapsedSeconds > 0) {
            val tokensToAdd = (elapsedSeconds * refillRatePerSecond).toInt()
            bucket.tokens = min(capacity, bucket.tokens + tokensToAdd)
            bucket.lastRefillTimeMillis = now
        }
    }
}
