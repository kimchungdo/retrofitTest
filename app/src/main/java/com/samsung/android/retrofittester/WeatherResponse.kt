package com.samsung.android.retrofittester

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float
)

data class Weather(
    val description: String
)


data class LocationResponse(
    val Key: String,
    val LocalizedName: String,
    val Country: Country
)

data class Country(
    val LocalizedName: String
)

data class CurrentConditionsResponse(
    val WeatherText: String,
    val Temperature: Temperature
)

data class Temperature(
    val Metric: Metric
)

data class Metric(
    val Value: Float,
    val Unit: String
)