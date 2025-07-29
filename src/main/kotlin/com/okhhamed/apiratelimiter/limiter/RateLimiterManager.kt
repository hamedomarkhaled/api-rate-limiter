package com.okhhamed.apiratelimiter.limiter

import com.okhhamed.apiratelimiter.config.AlgorithmType
import com.okhhamed.apiratelimiter.config.RateLimiterConfig

class RateLimiterManager {
    private var rateLimiter : RateLimiter = when(RateLimiterConfig.enabledAlgorithm) {
        AlgorithmType.TOKEN_BUCKET -> TokenBucketRateLimiter(RateLimiterConfig.TOKEN_BUCKET_CAPACITY, RateLimiterConfig.TOKEN_BUCKET_REFILL_RATE)
        AlgorithmType.LEAKY_BUCKET -> LeakyBucketRateLimiter(RateLimiterConfig.LEAKY_BUCKET_CAPACITY, RateLimiterConfig.LEAKY_BUCKET_LEAK_RATE)
        AlgorithmType.FIXED_WINDOW -> TODO("Implement Fixed Window")
        AlgorithmType.SLIDING_WINDOW -> SlidingWindowRateLimiter(RateLimiterConfig.MAX_REQUESTS, RateLimiterConfig.WINDOW_IN_MILLS)
    }

    fun allowRequest(clientId: String): Boolean = rateLimiter.allowRequest(clientId)
}