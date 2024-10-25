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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.willgo.data.Event
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.EventCard
import java.text.Normalizer

@Composable
fun SearchResultsScreen(paddingValues: PaddingValues, events: List<Event>, initialQuery: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit) {

    var query by remember { mutableStateOf(initialQuery) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TopBar()
            SearchBar(
                text = query,
                events = events,
                onQueryChange = { newQuery ->
                    query = newQuery
                },
                onSearch = {
                    onSearch(query)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Resultados de la búsqueda:",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 14.dp),
                fontSize = 16.sp
            )

            val filteredEvents = events.filter { event ->
                normalizeText(event.name_event).contains(normalizeText(query))
            }

            if (filteredEvents.isEmpty()) {
                Text("No se encontraron eventos.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredEvents) { event ->
                        CommonEventCard(event = event)  // Mostrar tarjeta de evento
                    }
                }
            }
        }
    }
}

@Composable
fun normalizeText(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        .lowercase()
}