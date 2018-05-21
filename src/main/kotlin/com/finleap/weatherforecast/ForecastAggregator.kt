package com.finleap.weatherforecast

import com.finleap.weatherforecast.owm.Main
import com.finleap.weatherforecast.owm.WeatherData
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

typealias TimeRange = Pair<ZonedDateTime, ZonedDateTime>

@Component
class ForecastAggregator(private val clock: Clock) {

    fun aggregate(weatherData: WeatherData): List<DailyAverage> {
        return (1..3).map { offset ->
            val average = averageParam(weatherData, offset)
            DailyAverage(
                    date = dayOffset(offset),
                    avgDayTemp = average(dayTempRange, Main::temp),
                    avgNightTemp = average(nightTempRange, Main::temp),
                    avgPressure = average(pressureRange, Main::pressure)
            )
        }
    }

    private fun averageParam(weatherData: WeatherData, offset: Int) =
            { range: (ZonedDateTime) -> TimeRange, extractor: (Main) -> Double ->
                val day = dayOffset(offset)
                val average = weatherData.list
                        .filter { epochInRange(it.dt, range(day)) }
                        .map { extractor(it.main) }
                        .average()
                when {
                    average.isFinite() -> average.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                    else -> BigDecimal.ZERO
                }
            }

    private fun epochInRange(epoch: Long, range: TimeRange): Boolean {
        val (start, end) = range
        return start.toEpochSecond() <= epoch && end.toEpochSecond() >= epoch
    }

    private val pressureRange = { day: ZonedDateTime -> Pair(day, day.plusDays(1)) }
    private val dayTempRange = { day: ZonedDateTime -> Pair(day.plusHours(6), day.plusHours(18)) }
    private val nightTempRange = { day: ZonedDateTime -> Pair(day.plusHours(18), day.plusDays(1).plusHours(6)) }

    private fun dayOffset(offset: Int) = ZonedDateTime.now(clock).plusDays(offset.toLong()).truncatedTo(ChronoUnit.DAYS)
}