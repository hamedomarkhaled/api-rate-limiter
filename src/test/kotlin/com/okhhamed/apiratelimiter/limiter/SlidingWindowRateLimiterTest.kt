package com.okhhamed.apiratelimiter.limiter

import kotlin.test.*
import java.util.concurrent.TimeUnit

class SlidingWindowRateLimiterTest {

    private lateinit var rateLimiter: SlidingWindowRateLimiter
    private val clientId = "test-client"

    @BeforeTest
    fun setup() {
        rateLimiter = SlidingWindowRateLimiter(maxRequests = 3, windowInMills = 2000)
    }

    @Test
    fun testWithinLimit() {
        repeat(3) {
            assertTrue(rateLimiter.allowRequest(clientId), "Request $it should be allowed")
        }
    }

    @Test
    fun testExceedingLimit() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        assertFalse(rateLimiter.allowRequest(clientId), "4th request should be denied within window")
    }

    @Test
    fun testSlidingWindowAllowsAfterExpiry() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        assertFalse(rateLimiter.allowRequest(clientId), "Should be denied immediately after hitting limit")

        // Wait for window to pass
        Thread.sleep(2100)

        assertTrue(rateLimiter.allowRequest(clientId), "Request should be allowed after window expiry")
    }

    @Test
    fun testPartialExpiryAllowsSome() {
        // 3 quick requests
        rateLimiter.allowRequest(clientId)
        Thread.sleep(500)
        rateLimiter.allowRequest(clientId)
        Thread.sleep(500)
        rateLimiter.allowRequest(clientId)

        assertFalse(rateLimiter.allowRequest(clientId), "Should be denied")

        // Wait until first request expires (after 2 seconds)
        Thread.sleep(1100)

        assertTrue(rateLimiter.allowRequest(clientId), "One old request should have expired")
    }
}
