package com.example.willgo.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.willgo.data.Event
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.EventCard

@Composable
fun SearchResultsScreen(paddingValues: PaddingValues, events: List<Event>) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TopBar()
            Text(
                text = "Resultados de la búsqueda",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 10.dp)
            )
                if (events.isEmpty()) {
                    Text("No se encontraron eventos.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(events) { event ->
                            CommonEventCard(event = event)  // Mostrar tarjeta de evento
                        }
                    }
                }
        }
    }
}