package com.finleap.weatherforecast.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(listOf(ConcurrentMapCache("weatherData")))
        return cacheManager
    }

    @CacheEvict(cacheNames = arrayOf("weatherData"), allEntries = true)
    @Scheduled(cron = "0 0 0 * * *")
    fun cacheEvict() {
    }
}