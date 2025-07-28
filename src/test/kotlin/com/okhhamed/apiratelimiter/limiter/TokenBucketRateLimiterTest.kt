package com.okhhamed.apiratelimiter.limiter

import kotlin.test.*

class TokenBucketRateLimiterTest {

    private lateinit var rateLimiter: TokenBucketRateLimiter
    private val clientId = "test-client"

    @BeforeTest
    fun setup() {
        // Config: capacity = 3 tokens, refill 1 token per second
        rateLimiter = TokenBucketRateLimiter(capacity = 3, refillRatePerSecond = 1)
    }

    @Test
    fun testWithinCapacity() {
        repeat(3) {
            assertTrue(rateLimiter.allowRequest(clientId), "Request $it should be allowed")
        }
    }

    @Test
    fun testExceedingCapacity() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        // This request should be denied immediately (no refill)
        assertFalse(rateLimiter.allowRequest(clientId), "4th request should be denied")
    }

    @Test
    fun testTokenRefill() {
        repeat(3) { rateLimiter.allowRequest(clientId) }
        assertFalse(rateLimiter.allowRequest(clientId), "Bucket should be empty")

        Thread.sleep(2100) // Wait for 2 seconds (2 tokens should be refilled)

        assertTrue(rateLimiter.allowRequest(clientId), "1st refill request should be allowed")
        assertTrue(rateLimiter.allowRequest(clientId), "2nd refill request should be allowed")
        assertFalse(rateLimiter.allowRequest(clientId), "3rd request should be denied (only 2 tokens)")
    }
}
