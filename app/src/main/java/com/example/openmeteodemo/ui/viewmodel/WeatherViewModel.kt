package com.example.openmeteodemo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openmeteodemo.data.model.WeatherResponse
import com.example.openmeteodemo.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()
    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = weatherRepository.getWeather(latitude, longitude)
                _weatherState.value = response
                Log.d("WeatherViewModel", "Respuesta del API OPEN METEO: $response")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error al obtener el clima", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
