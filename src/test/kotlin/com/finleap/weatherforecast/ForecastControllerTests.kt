package com.finleap.weatherforecast

import arrow.core.Try
import com.finleap.weatherforecast.owm.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.HttpClientErrorException
import java.math.BigDecimal
import java.time.ZoneOffset
import java.time.ZonedDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ForecastControllerTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @MockBean
    lateinit var openWeatherMapService: OpenWeatherMapService

    @Test
    fun shouldReturnsValidDailyTemp() {
        val today = epochTime(2018, 1, 2)
        val tomorrow = epochTime(2018, 1, 3)

        given(openWeatherMapService.weatherData("London"))
                .willReturn(Try.just(WeatherData(City("London"), listOf(
                        Item(today(5, 30), Main(temp = 10.0, pressure = 1010.0)),
                        Item(today(8, 30), Main(temp = 20.0, pressure = 1020.0)),
                        Item(today(12, 30), Main(temp = 30.0, pressure = 1030.0)),
                        Item(today(18, 30), Main(temp = 40.0, pressure = 1040.0)),
                        Item(tomorrow(8, 30), Main(temp = 50.0, pressure = 1050.0))
                ))))

        val response = restTemplate.getForEntity<Forecast>("/data?city=London")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.city).isEqualTo("London")
        assertThat(response.body!!.forecast)
                .hasSize(3)
                .contains(
                        DailyAverage(
                                date = ZonedDateTime.parse("2018-01-02T00:00Z[UTC]"),
                                avgPressure = BigDecimal("1025.00"),
                                avgNightTemp = BigDecimal("40.00"),
                                avgDayTemp = BigDecimal("25.00")
                        ))
    }

    @Test
    fun shouldReturnNotFound() {
        given(openWeatherMapService.weatherData(anyString()))
                .willReturn(Try.raise(HttpClientErrorException(HttpStatus.NOT_FOUND)))

        val response = restTemplate.getForEntity<Any>("/data?city=InvalidName")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    private fun epochTime(year: Int, month: Int, day: Int) = {
        hour: Int, minute: Int ->
        ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneOffset.UTC).toEpochSecond()
    }

}
