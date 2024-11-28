package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class SharedCar(
    val eventId: Int,            // Asociado al evento
    val userId: String,           // ID del usuario que lo creó
    val seatsAvailable: Int,      // Plazas libres
    val departureTime: String,    // Hora de salida
    val departureAddress: String  // Dirección de salida
)

