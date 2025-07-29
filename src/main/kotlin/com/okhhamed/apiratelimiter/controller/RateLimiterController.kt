package com.okhhamed.apiratelimiter.controller

import com.okhhamed.apiratelimiter.limiter.RateLimiterManager
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class RateLimiterController {
    private val logger = LoggerFactory.getLogger(RateLimiterController::class.java)
    private val rateLimiterManager = RateLimiterManager()

    @GetMapping("/api/request")
    fun handleRequest(@RequestParam clientId: String) : String {
        val allowed = rateLimiterManager.allowRequest(clientId)
        return if(allowed) "Request allowed\n" else "Too many requests, try later\n"
    }
}