package com.okhhamed.apiratelimiter.limiter


import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class SlidingWindowRateLimiter(
    private val maxRequests: Int,
    private val windowInMills: Long,
): RateLimiter {
    private val logger = LoggerFactory.getLogger(SlidingWindowRateLimiter::class.java)
    private val requestLogs = ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>>()

    override fun allowRequest(clientId: String): Boolean {
        val now = System.currentTimeMillis()
        val windowStart = now - windowInMills
        val logs = requestLogs.computeIfAbsent(clientId) { ConcurrentLinkedQueue() }

        while(logs.peek() != null && logs.peek() < windowStart) {
            logs.poll()
        }

        return if(logs.size < maxRequests) {
            logs.add(now)
            logger.info("✅ Request allowed for client $clientId | Total in window: ${logs.size}")
            true
        } else {
            logger.warn("❌ Request denied for client $clientId | Total in window: ${logs.size}")
            false
        }
    }
}