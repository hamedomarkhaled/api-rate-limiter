package com.okhhamed.apiratelimiter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiRateLimiterApplication

fun main(args: Array<String>) {
    runApplication<ApiRateLimiterApplication>(*args)
}
