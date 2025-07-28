package com.okhhamed.apiratelimiter.limiter

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class LeakyBucketRateLimiter(private val capacity: Int, private val leakRatePerSecond: Int): RateLimiter {
    private val logger = LoggerFactory.getLogger(LeakyBucketRateLimiter::class.java)

    private data class Bucket(
        var lastLeakTimestamp: Long,
        var currentQueueSize: Int,
    )

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun allowRequest(clientId: String): Boolean {
        val now = System.nanoTime()
        val bucket = buckets.computeIfAbsent(clientId) {
            Bucket(lastLeakTimestamp = now, currentQueueSize = 0)
        }
        synchronized(bucket) {
            leak(bucket, now)
            return if (bucket.currentQueueSize < capacity) {
                bucket.currentQueueSize++
                logger.info("✅ Request allowed for $clientId | Queue size: ${bucket.currentQueueSize}")
                true
            } else {
                logger.warn("❌ Request denied for $clientId | Queue full: ${bucket.currentQueueSize}")
                false
            }
        }
    }

    private fun leak(bucket: Bucket, now: Long) {
        val elapsedSeconds = (now - bucket.lastLeakTimestamp).toDouble() / 1_000_000_000
        val leaked = (elapsedSeconds * leakRatePerSecond).toInt()

        if(leaked > 0) {
            bucket.currentQueueSize = maxOf(0, bucket.currentQueueSize - leaked)
            bucket.lastLeakTimestamp = now
        }
    }
}