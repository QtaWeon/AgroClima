// En: ui/viewmodel/WeatherDecisionViewModel.kt
package com.example.openmeteodemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.openmeteodemo.data.model.*

class WeatherDecisionViewModel : ViewModel() {

    fun procesarDatosReales(weatherResponse: WeatherResponse?): DecisionesClima? {
        if (weatherResponse == null) return null

        val current = weatherResponse.currentWeather

        return DecisionesClima(
            recomendaciones = getRecomendacionesFromWeatherCode(
                current.weathercode,
                current.isDay,
                current.temperature,
                current.windspeed
            ),
            alertas = getAlertasFromCurrentWeather(current),
            datosUtilizados = listOf(
                "Weather Code: ${current.weathercode}",
                "Temperatura: ${current.temperature}°C",
                "Viento: ${current.windspeed} km/h",
                if (current.isDay == 1) "Es de día" else "Es de noche"
            )
        )
    }

    private fun getRecomendacionesFromWeatherCode(
        weathercode: Int,
        isDay: Int,
        temperatura: Double,
        viento: Double
    ): List<String> {
        val recomendaciones = mutableListOf<String>()

        when (weathercode) {
            0, 1, 2 -> {
                recomendaciones.add("🌞 Buen día para actividades al aire libre")
                if (isDay == 1) {
                    recomendaciones.add("☀️ Condiciones óptimas para labores de campo")
                    recomendaciones.add("✅ Buen momento para aplicaciones foliares")
                }
            }
            3 -> {
                recomendaciones.add("⛅ Día nublado - buen momento para trasplantes")
                recomendaciones.add("💧 Menor evaporación - reducir riego")
            }
            45, 48 -> {
                recomendaciones.add("🌫️ Conducir con precaución - visibilidad reducida")
                recomendaciones.add("⚠️ Alta humedad - monitorear cultivos sensibles")
            }
            in 51..57 -> {
                recomendaciones.add("🌦️ Llevar paraguas o impermeable")
                recomendaciones.add("💧 Precipitación leve - beneficiosa para plantas")
                recomendaciones.add("⏸️ Postergar aplicaciones de productos")
            }
            in 61..67 -> {
                recomendaciones.add("🌧️ Evitar actividades exteriores")
                recomendaciones.add("🚗 Conducir con precaución - piso mojado")
                recomendaciones.add("🚫 Alto riesgo de lavado de nutrientes")
            }
            in 95..99 -> {
                recomendaciones.add("⛈️ ALERTA: Buscar refugio inmediatamente")
                recomendaciones.add("🔌 Desconectar equipos eléctricos")
                recomendaciones.add("🚨 Suspender todas las actividades de campo")
            }
        }

        when {
            temperatura > 35.0 -> {
                recomendaciones.add("🌡️ Temperatura alta - riesgo de estrés térmico en cultivos")
                recomendaciones.add("💦 Aumentar frecuencia de riego")
            }
            temperatura < 10.0 -> {
                recomendaciones.add("❄️ Temperatura baja - proteger cultivos sensibles")
                recomendaciones.add("🏠 Considerar cubiertas protectoras")
            }
        }

        if (viento > 25.0) {
            recomendaciones.add("💨 Vientos fuertes - asegurar estructuras y cultivos altos")
        }

        return recomendaciones
    }

    private fun getAlertasFromCurrentWeather(current: CurrentWeather): List<Alerta> {
        val alertas = mutableListOf<Alerta>()

        when {
            current.temperature > 38.0 -> alertas.add(
                Alerta(
                    "🌡️ Calor extremo",
                    "Temperatura muy alta - riesgo para cultivos y personas",
                    "ALTA"
                )
            )
            current.temperature < 5.0 -> alertas.add(
                Alerta(
                    "❄️ Frío intenso",
                    "Temperatura muy baja - proteger cultivos sensibles",
                    "MEDIA"
                )
            )
        }

        if (current.windspeed > 30.0) {
            alertas.add(
                Alerta(
                    "💨 Vientos fuertes",
                    "Vientos intensos pueden dañar cultivos y estructuras",
                    "MEDIA"
                )
            )
        }

        when (current.weathercode) {
            in 95..99 -> alertas.add(
                Alerta(
                    "⛈️ Tormenta eléctrica",
                    "Tormenta severa - buscar refugio seguro inmediatamente",
                    "CRÍTICA"
                )
            )
            in 71..77 -> alertas.add(
                Alerta(
                    "❄️ Condiciones inusuales",
                    "Evento climático atípico para la región",
                    "MEDIA"
                )
            )
        }

        return alertas
    }
}