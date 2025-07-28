package com.okhhamed.apiratelimiter.limiter

import kotlin.test.*

class LeakyBucketRateLimiterTest {

    private lateinit var rateLimiter: LeakyBucketRateLimiter
    private val clientId = "test-client"

    @BeforeTest
    fun setup() {
        // Bucket can hold 3 requests, leaks 1 per second
        rateLimiter = LeakyBucketRateLimiter(capacity = 3, leakRatePerSecond = 1)
    }

    @Test
    fun testWithinCapacity() {
        repeat(3) {
            assertTrue(rateLimiter.allowRequest(clientId), "Request $it should be allowed")
            Thread.sleep(10)
        }
    }

    @Test
    fun testExceedingCapacity() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        Thread.sleep(10)
        assertFalse(rateLimiter.allowRequest(clientId), "4th request should be denied (bucket full)")
    }

    @Test
    fun testLeakAndAllowAfterWait() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        assertFalse(rateLimiter.allowRequest(clientId), "Bucket should be full")

        Thread.sleep(2100) // Wait enough time to leak ~2 requests

        assertTrue(rateLimiter.allowRequest(clientId), "Should allow after leakage")
        assertTrue(rateLimiter.allowRequest(clientId), "Should allow second after leakage")
        assertFalse(rateLimiter.allowRequest(clientId), "Bucket should be full again")
    }
}
