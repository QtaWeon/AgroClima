package com.example.openmeteodemo.data.model

data class DecisionesClima(
    val recomendaciones: List<String>,
    val alertas: List<Alerta>,
    val datosUtilizados: List<String>
)

data class Alerta(
    val titulo: String,
    val mensaje: String,
    val nivel: String
)

enum class CultivoParaguayo {
    SOJA, MAIZ, MANDIOCA, TRIGO, CANA_AZUCAR
}