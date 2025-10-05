package com.example.openmeteodemo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.openmeteodemo.data.model.Alerta
import com.example.openmeteodemo.data.model.DecisionesClima

@Composable
fun AlertCard(alerta: Alerta, modifier: Modifier = Modifier) {
    val color = when (alerta.nivel) {
        "CRÍTICA" -> Color(0xFFFF5252)
        "ALTA" -> Color(0xFFFF9800)
        "MEDIA" -> Color(0xFFFFC107)
        else -> Color(0xFF757575)
    }

    Card(
        modifier = modifier,
        border = BorderStroke(2.dp, color),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = alerta.titulo,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = alerta.mensaje)
        }
    }
}

@Composable
fun RecomendacionCard(recomendacion: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
    ) {
        Text(
            text = recomendacion,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun AgroClimaSection(decisiones: DecisionesClima) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "📊 Análisis basado en:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        decisiones.datosUtilizados.forEach { dato ->
            Text(
                text = "• $dato",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "💡 Recomendaciones:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        decisiones.recomendaciones.forEach { recomendacion ->
            RecomendacionCard(
                recomendacion = recomendacion,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }

        if (decisiones.alertas.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "⚠️ Alertas:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            decisiones.alertas.forEach { alerta ->
                AlertCard(
                    alerta = alerta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}