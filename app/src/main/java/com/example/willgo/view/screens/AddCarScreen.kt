package com.example.willgo.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AddCarScreen(eventId: Int, onCarAdded: () -> Unit) {
    var seatsAvailable by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }
    var departureAddress by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Añadir coche compartido")

        // Campos del formulario
        OutlinedTextField(
            value = seatsAvailable,
            onValueChange = { seatsAvailable = it },
            label = { Text("Plazas disponibles") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = departureTime,
            onValueChange = { departureTime = it },
            label = { Text("Hora de salida") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = departureAddress,
            onValueChange = { departureAddress = it },
            label = { Text("Dirección de salida") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para guardar el coche
        Button(onClick = {
            if (seatsAvailable.isNotEmpty() && departureTime.isNotEmpty() && departureAddress.isNotEmpty()) {
                coroutineScope.launch {
                    addCarToDatabase(
                        eventId = eventId,
                        seatsAvailable = seatsAvailable.toInt(),
                        departureTime = departureTime,
                        departureAddress = departureAddress
                    ) {
                        onCarAdded() // Actualizar la UI tras añadir el coche
                    }
                }
            }
        }) {
            Text("Añadir coche")
        }
    }
}