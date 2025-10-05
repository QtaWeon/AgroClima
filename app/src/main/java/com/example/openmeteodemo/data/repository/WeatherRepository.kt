package com.example.openmeteodemo.data.repository

import android.util.Log
import com.example.openmeteodemo.data.model.WeatherResponse
import com.example.openmeteodemo.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class WeatherRepository {

    private val baseUrl = "https://api.open-meteo.com/v1/"

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            // Construimos la URL manualmente para depurar
            val url = "${baseUrl}forecast?latitude=$latitude&longitude=$longitude&current_weather=true&hourly=temperature_2m,precipitation,windspeed_10m"
            Log.d("WeatherRepository", "📡 Request URL: $url")

            try {
                // Llamada real usando Retrofit
                val response = RetrofitInstance.api.getForecast(latitude, longitude)
                Log.d("WeatherRepository", "✅ Parsed Response: $response")

                // También obtenemos JSON crudo manualmente para depuración
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val rawResponse = retrofit.callFactory()
                    .newCall(Request.Builder().url(url).build())
                    .execute()
                    .body()
                    ?.string()

                Log.d("WeatherRepository", "🧾 Raw JSON:\n$rawResponse")

                response
            } catch (e: Exception) {
                Log.e("WeatherRepository", "❌ Error: ${e.message}")
                throw e
            }
        }
    }
}