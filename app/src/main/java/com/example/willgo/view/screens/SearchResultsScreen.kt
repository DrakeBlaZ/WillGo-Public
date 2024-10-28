package com.example.willgo.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.willgo.data.Event
import com.example.willgo.graphs.BottomBarScreen
import com.example.willgo.graphs.Graph
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.EventCard
import com.example.willgo.view.sections.FiltersView
import java.text.Normalizer

@Composable
fun SearchResultsScreen(paddingValues: PaddingValues, events: List<Event>, initialQuery: String, onQueryChange: (String) -> Unit, onSearch: (String) -> Unit, navController: NavController) {

    var query by remember { mutableStateOf(initialQuery) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.White)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            TopBar(navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}){ Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ArrowBack") }
            })
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

            FiltersView()
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    items(filteredEvents) { event ->
                        CommonEventCard(event = event)  // Mostrar tarjeta de evento
                    }
                }
            }
        }
    }
}

fun normalizeText(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace("\\p{M}".toRegex(), "")  // Elimina diacríticos (acentos)
        .replace("[^\\p{ASCII}]".toRegex(), "") // Elimina caracteres no ASCII
        .lowercase()  // Convierte el texto a minúsculas
}


