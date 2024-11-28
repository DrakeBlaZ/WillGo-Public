package com.example.willgo.view.screens

import android.annotation.SuppressLint

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import android.util.Log
import androidx.compose.foundation.layout.*    // Para los espacios y layouts
import androidx.compose.foundation.lazy.LazyColumn  // Para listas optimizadas
import androidx.compose.foundation.lazy.items       // Para iterar sobre listas en Compose
import androidx.compose.material.*            // Para componentes de Material Design como Card, Button, etc.
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*             // Para estados como `remember` y `LaunchedEffect`
import androidx.compose.ui.Modifier           // Para modificaciones de tamaño y layout
import androidx.compose.ui.unit.dp            // Para definir márgenes, paddings, etc.
import com.example.willgo.data.SharedCar




@Composable
fun CarListScreen(eventId: Int, onAddCarClicked: () -> Unit) {
    val client = getClient()
    var carList by remember { mutableStateOf(emptyList<SharedCar>()) }

    LaunchedEffect(eventId) {
        try {
            val response = client.postgrest["Coche_compartido"].select {
                filter{
                    eq("event_id", eventId) // Filtra por event_id
                }
            }
                .decodeList<SharedCar>()

            carList = response
        } catch (e: Exception) {
            Log.e("CarListScreen", "Error al obtener la lista de coches: $e")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Coches compartidos")

        LazyColumn {
            items(carList) { car ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Usuario: ${car.userId}")
                        Text("Plazas disponibles: ${car.seatsAvailable}")
                        Text("Hora de salida: ${car.departureTime}")
                        Text("Dirección: ${car.departureAddress}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para añadir un coche
        Button(onClick = onAddCarClicked) {
            Text("Añadir mi coche")
        }
    }
}



suspend fun addCarToDatabase(
    eventId: Int,
    seatsAvailable: Int,
    departureTime: String,
    departureAddress: String,
    onSuccess: () -> Unit
) {
    val user = getUser() // Obtener el usuario actual
    val client = getClient() // Obtener el cliente de Supabase

    // Crear el objeto SharedCar sin incluir el ID
    val car = SharedCar(
        eventId = eventId,
        userId = user.nickname ?: "",
        seatsAvailable = seatsAvailable,
        departureTime = departureTime,
        departureAddress = departureAddress
    )

    try {
        // Insertar el nuevo coche en la base de datos
        client.postgrest["Coche_compartido"].insert(car)
        onSuccess() // Llamar a la función de éxito para actualizar la UI
    } catch (e: Exception) {
        Log.e("addCarToDatabase", "Error al añadir el coche compartido: $e")
    }
}

