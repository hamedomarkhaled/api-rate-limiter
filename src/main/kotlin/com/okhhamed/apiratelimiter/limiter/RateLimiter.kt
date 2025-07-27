package com.okhhamed.apiratelimiter.limiter

interface RateLimiter {
    fun allowRequest(clientId: String): Boolean
}