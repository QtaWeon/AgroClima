package com.example.openmeteodemo.data.model

import com.squareup.moshi.Json

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @Json(name = "generationtime_ms") val generationTimeMs: Double,
    @Json(name = "utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @Json(name = "timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @Json(name = "current_weather") val currentWeather: CurrentWeather,
    val hourly: HourlyData? = null
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Int,
    @Json(name = "is_day") val isDay: Int,
    val time: String,
    val weathercode: Int
)

data class HourlyData(
    val time: List<String>,
    @Json(name = "temperature_2m") val temperature2m: List<Double>,
    val precipitation: List<Double>,
    @Json(name = "windspeed_10m") val windspeed10m: List<Double>
)

