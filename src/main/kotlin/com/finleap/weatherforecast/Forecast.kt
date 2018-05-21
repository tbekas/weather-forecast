package com.finleap.weatherforecast

import java.math.BigDecimal
import java.time.ZonedDateTime

data class DailyAverage(val date: ZonedDateTime,
                        val avgDayTemp: BigDecimal,
                        val avgNightTemp: BigDecimal,
                        val avgPressure: BigDecimal)

data class Forecast(val city: String,
                    val forecast: List<DailyAverage>)