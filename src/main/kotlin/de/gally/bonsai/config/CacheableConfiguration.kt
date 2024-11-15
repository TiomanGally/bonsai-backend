package de.gally.bonsai.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

@EnableCaching
@EnableScheduling
@Configuration
class CacheableConfiguration {

    companion object {
        const val WEATHER_CACHE_NAME = "weather"
        private val LOGGER: Logger = LoggerFactory.getLogger(CacheableConfiguration::class.java)
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(WEATHER_CACHE_NAME)
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay = 0, fixedRateString = "\${bonsai.weather.cache.interval}")
    @CacheEvict(cacheNames = [WEATHER_CACHE_NAME], allEntries = true, beforeInvocation = false)
    fun scheduledCacheCleaner() {
        LOGGER.info("Weather Cache will be cleared")
    }
}
