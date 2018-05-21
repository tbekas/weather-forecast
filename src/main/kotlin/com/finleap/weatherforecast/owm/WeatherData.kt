package com.finleap.weatherforecast.owm

data class City(val name: String)

data class Main(val temp: Double, val pressure: Double)

data class Item(val dt: Long, val main: Main)

data class WeatherData(
        val city: City,
        val list: List<Item>
)