package com.example.openmeteodemo.helpers

fun getWeatherDescription(weatherCode: Int): Pair<String, String> {
    return when(weatherCode) {
        0 -> "Despejado" to "🌞"
        1 -> "Mayormente despejado" to "🌤️"
        2 -> "Parcialmente nublado" to "⛅"
        3 -> "Nublado" to "☁️"
        45 -> "Niebla" to "🌫️"
        48 -> "Niebla" to "🌫️"
        in 51..57 -> "Llovizna" to "🌦️"
        in 61..67 -> "Lluvia" to "🌧️"
        in 71..77 -> "Nieve" to "🌨️"
        in 80..82 -> "Chubascos" to "🌧️"
        in 85..86 -> "Nevadas" to "❄️"
        in 95..99 -> "Tormenta" to "⛈️"
        else -> "Desconocido" to "❓"
    }
}