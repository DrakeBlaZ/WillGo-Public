package com.example.willgo.view.screens.navScreens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.willgo.data.Event
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

@Composable
fun MapScreen(
    eventsState: MutableState<List<Event>>,
    onEventClick: (Event) -> Unit
){
//    val marker = LatLng(39.482298, -0.346236)
//    val marker1 = LatLng(45.482298, -5.346236)
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    val events by eventsState
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = uiSettings
    ){
//        Marker(position = marker)
//        Marker(position = marker1)
        events.forEach { event ->
            if (event.latitude != null && event.longitude != null) {
                val eventLocation = LatLng(event.latitude, event.longitude)

                Marker(
                    position = eventLocation,
                    title = event.name_event,
                    snippet = "${event.date ?: ""} - ${event.category?.name ?: ""}",
                    onClick = {
                        // pongo accion con el click
                        // por ejemplo, navegar a la pantalla con detalles e evento
                        onEventClick(event)
                        true
                    }
                )
            }
        }
    }
}