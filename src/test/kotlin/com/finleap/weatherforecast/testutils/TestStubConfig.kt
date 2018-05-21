package com.finleap.weatherforecast.testutils

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@Configuration
class TestStubConfig {

    @Bean
    @Primary
    fun fixedClock(): Clock {
        return Clock.fixed(Instant.parse("2018-01-01T18:30:00Z"), ZoneOffset.UTC)
    }
}