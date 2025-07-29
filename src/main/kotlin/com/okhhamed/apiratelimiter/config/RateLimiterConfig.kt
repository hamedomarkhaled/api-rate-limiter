package com.okhhamed.apiratelimiter.config


enum class AlgorithmType {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW,
}


object RateLimiterConfig {
    val enabledAlgorithm : AlgorithmType = AlgorithmType.SLIDING_WINDOW

    // Token Bucket specific configs
    const val TOKEN_BUCKET_CAPACITY = 3
    const val TOKEN_BUCKET_REFILL_RATE = 1

    // Leaky Bucket specific configs
    const val LEAKY_BUCKET_CAPACITY = 10
    const val LEAKY_BUCKET_LEAK_RATE = 2

    // Sliding window specific configs
    const val MAX_REQUESTS = 5
    const val WINDOW_IN_MILLS = 3000L
}