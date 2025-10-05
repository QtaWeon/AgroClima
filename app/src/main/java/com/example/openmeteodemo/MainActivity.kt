package com.example.openmeteodemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.tooling.preview.Preview
import com.example.openmeteodemo.data.model.CurrentWeather
import com.example.openmeteodemo.data.model.WeatherResponse
import com.example.openmeteodemo.helpers.getWeatherDescription
import com.example.openmeteodemo.ui.viewmodel.WeatherViewModel
import com.example.openmeteodemo.ui.viewmodel.WeatherDecisionViewModel
import com.example.openmeteodemo.ui.components.AgroClimaSection
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) getCurrentLocation()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherScreen(viewModel, onRefreshLocation = { getCurrentLocation() })
        }

        checkPermissionsAndFetchLocation()
    }

    private fun checkPermissionsAndFetchLocation() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                viewModel.fetchWeather(latitude = lat, longitude = lon)
            } else {
                viewModel.fetchWeather(latitude = -25.2637, longitude = -57.5759)
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, onRefreshLocation: () -> Unit) {
    val weather by viewModel.weatherState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    WeatherContent(
        weather = weather,
        isLoading = isLoading,
        onRefresh = onRefreshLocation
    )
}

@Composable
fun WeatherContent(
    weather: WeatherResponse?,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    val decisionViewModel = remember { WeatherDecisionViewModel() }
    val decisiones = remember(weather) {
        decisionViewModel.procesarDatosReales(weather)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Clima Actual", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Obteniendo datos del clima...")
                }

                weather != null -> {
                    val data = weather.currentWeather
                    val (description, emoji) = getWeatherDescription(data.weathercode)

                    Text("Temperatura: ${data.temperature}°C")
                    Text("Viento: ${data.windspeed} km/h")
                    Text("Condición: $description $emoji")
                    Text("Hora: ${data.time}")

                    Spacer(modifier = Modifier.height(24.dp))

                    decisiones?.let {
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text("🌱 Análisis Agrícola", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        AgroClimaSection(decisiones = it)
                    }
                }

                else -> {
                    Text("Sin datos disponibles.")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRefresh,
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Actualizando..." else "Refrescar 🌍")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    val mockWeather = WeatherResponse(
        latitude = -25.2637,
        longitude = -57.5759,
        generationTimeMs = 10.2,
        utcOffsetSeconds = -10800,
        timezone = "America/Asuncion",
        timezoneAbbreviation = "PYT",
        elevation = 120.0,
        currentWeather = CurrentWeather(
            temperature = 38.5,
            windspeed = 35.0,
            winddirection = 180,
            isDay = 1,
            weathercode = 95,
            time = "2025-10-05T12:00"
        )
    )
    WeatherContent(weather = mockWeather, isLoading = false, onRefresh = {})
}