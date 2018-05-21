package com.finleap.weatherforecast.owm

import arrow.core.Try
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Component
class OpenWeatherMapService(
        private val properties: OpenWeatherMapProperties,
        private val restTemplate: RestTemplate) {

    @Cacheable("weatherData")
    fun weatherData(city: String): Try<WeatherData> {
        val params = mapOf("city" to city, "api-key" to properties.apiKey)
        return Try { restTemplate.getForObject<WeatherData>(properties.url, params)!! }
    }

}