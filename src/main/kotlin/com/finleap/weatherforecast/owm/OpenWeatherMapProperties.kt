package com.finleap.weatherforecast.owm

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("app.openweathermap")
class OpenWeatherMapProperties(var url: String,
                               var apiKey: String) {
    constructor(): this("", "")
}