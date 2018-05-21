package com.finleap.weatherforecast

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.Clock


@EnableConfigurationProperties
@EnableSwagger2
@EnableCaching
@EnableScheduling
@SpringBootApplication
class WeatherForecastApplication {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}

fun main(args: Array<String>) {
    runApplication<WeatherForecastApplication>(*args)
}
