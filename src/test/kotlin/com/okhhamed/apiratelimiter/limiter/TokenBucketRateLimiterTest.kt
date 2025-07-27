package com.okhhamed.apiratelimiter.limiter

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TokenBucketRateLimiterTest {
    private lateinit var rateLimiter: TokenBucketRateLimiter
    private val clientId = "test-client"

    @BeforeTest
    fun setup() {
        rateLimiter = TokenBucketRateLimiter(capacity = 3, refillRatePerSecond = 1)
    }

    @Test
    fun testAllowWithinCapacity(){
        repeat(3) {
            assertTrue(rateLimiter.allowRequest(clientId), "Request $it should be allowed")
        }
    }

    @Test
    fun testRefillTokens() {
        // Use all tokens
        repeat(3) {
            assertTrue(rateLimiter.allowRequest(clientId))
        }
        assertFalse(rateLimiter.allowRequest(clientId))

        // Wait enough time for 2 tokens to refill
        Thread.sleep(2100)

        // Now 2 requests should be allowed again
        repeat(2) {
            assertTrue(rateLimiter.allowRequest(clientId))
        }
    }


}