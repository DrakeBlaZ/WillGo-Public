package com.example.willgo.view.screens.navScreens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.willgo.data.Event
import com.example.willgo.data.FavoriteEvent
import com.example.willgo.graphs.getFavoriteEventIds
import com.example.willgo.view.screens.getClient
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import io.github.jan.supabase.postgrest.postgrest

@Composable
fun MapScreen(
    eventsState: MutableState<List<Event>>,
    onEventClick: (Event) -> Unit
){

    val coroutineScope = rememberCoroutineScope()
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    val events by eventsState
    var favoriteEvents by remember { mutableStateOf<List<Long>>(emptyList()) }

    LaunchedEffect(Unit) {
        favoriteEvents = getFavoriteEventIds()
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = uiSettings
    ){
        events.forEach { event ->
            if (event.latitude != null && event.longitude != null) {
                val eventLocation = LatLng(event.latitude, event.longitude)
                val isFavorite = event.id in favoriteEvents

                Marker(
                    position = eventLocation,
                    title = event.name_event,
                    snippet = "${event.date ?: ""} - ${event.category?.name ?: ""}",
                    icon = if (isFavorite) BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    else BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
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