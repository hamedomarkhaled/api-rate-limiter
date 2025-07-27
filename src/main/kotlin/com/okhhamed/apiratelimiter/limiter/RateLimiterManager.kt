package com.okhhamed.apiratelimiter.limiter

import com.okhhamed.apiratelimiter.config.AlgorithmType
import com.okhhamed.apiratelimiter.config.RateLimiterConfig

class RateLimiterManager {
    private var rateLimiter : RateLimiter = when(RateLimiterConfig.enabledAlgorithm) {
        AlgorithmType.TOKEN_BUCKET -> TokenBucketRateLimiter(RateLimiterConfig.TOKEN_BUCKET_CAPACITY, RateLimiterConfig.TOKEN_BUCKET_REFILL_RATE)
        AlgorithmType.LEAKY_BUCKET -> TODO("Implement Leaky Bucket")
        AlgorithmType.FIXED_WINDOW -> TODO("Implement Fixed Window")
        AlgorithmType.SLIDING_WINDOW -> TODO("Implement Sliding Window")
    }

    fun allowRequest(clientId: String): Boolean = rateLimiter.allowRequest(clientId)
}