package com.finleap.weatherforecast

import arrow.core.getOrElse
import com.finleap.weatherforecast.owm.OpenWeatherMapService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

@RestController
class ForecastController(private val openWeatherMapService: OpenWeatherMapService,
                         private val forecastAggregator: ForecastAggregator) {

    @GetMapping("data")
    fun forecast(@RequestParam("city") city: String): ResponseEntity<Forecast> {
        val weatherData = openWeatherMapService.weatherData(city)

        return weatherData
                .map { forecastAggregator.aggregate(it) }
                .map { Forecast(city, it) }
                .map { ResponseEntity.ok().body(it) }
                .getOrElse { ex: Throwable ->
                    when (ex) {
                        is HttpClientErrorException -> ResponseEntity.status(ex.statusCode).build()
                        else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                    }
                }
    }
}