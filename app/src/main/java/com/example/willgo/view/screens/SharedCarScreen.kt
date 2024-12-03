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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*             // Para estados como `remember` y `LaunchedEffect`
import androidx.compose.ui.Modifier           // Para modificaciones de tamaño y layout
import androidx.compose.ui.unit.dp            // Para definir márgenes, paddings, etc.
import com.example.willgo.data.SharedCar
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(eventId: Int, onAddCarClicked: () -> Unit, onBack: () -> Unit) {
    val client = getClient()
    var carList by remember { mutableStateOf(emptyList<SharedCar>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(eventId) {
        coroutineScope.launch {
            carList = getCarList(eventId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coches disponibles") },
                navigationIcon = {
                    IconButton(onClick = onBack) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        LazyColumn {
            items(carList) { sharedCar ->
                Card(modifier = Modifier.fillMaxWidth().padding(paddingValues)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Usuario: ${sharedCar.userId}")
                        Text("Plazas disponibles: ${sharedCar.seatsAvailable}")
                        Text("Hora de salida: ${sharedCar.departureTime}")
                        Text("Dirección: ${sharedCar.departureAddress}")
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
        Log.d("OOOOOOOOOOOOOOOOOO", "Datos del coche: eventId=$eventId, userId=${user.nickname}, seatsAvailable=$seatsAvailable, departureTime=$departureTime, departureAddress=$departureAddress")
        client.postgrest["Coche_compartido"].insert(car)
        onSuccess() // Llamar a la función de éxito para actualizar la UI
    } catch (e: Exception) {
        Log.e("addCarToDatabase", "Error al añadir el coche compartido: $e")
    }
}

