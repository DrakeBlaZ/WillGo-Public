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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.graphs.BottomBarScreen
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.FiltersPreview
import com.example.willgo.view.sections.FiltersTagView
import kotlinx.datetime.LocalDate
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    paddingValues: PaddingValues,
    events: List<Event>,
    initialQuery: String,
    initialCategory: Category? = null,
    maxPrice: Float? = null,
    externalSelectedCategory: Category? = null,
    typeFilter: String? = null,
    dateFilter: String? = null,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navController: NavController
) {

    var query by remember { mutableStateOf(initialQuery) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Obtenemos las fechas de hoy, de la semana y del mes siguiente
    val today = dateFormatter.parse(dateFormatter.format(Calendar.getInstance().time)) ?: Calendar.getInstance().time
    val nextWeek = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }.time
    val nextMonth = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }.time

    val categoryToFilter = externalSelectedCategory ?: selectedCategory

    // Filtrado de eventos basado en categoría, precio, tipo y fecha
    val filteredEvents = events.filter { event ->
        val eventDate = try {
            dateFormatter.parse(event.date ?: "")
        } catch (e: Exception) {
            null
        }

        eventDate != null &&
                (categoryToFilter == null || event.category == categoryToFilter) &&
                (maxPrice == null || (event.price ?: 0f) <= maxPrice) &&
                (typeFilter == null || event.type.equals(typeFilter, ignoreCase = true)) &&
                (dateFilter == null ||
                        (dateFilter == "Hoy" && eventDate.compareTo(today) == 0) ||
                        (dateFilter == "Esta semana" && eventDate in today..nextWeek) ||
                        (dateFilter == "Este mes" && eventDate in today..nextMonth) ||
                        (dateFilter != "Hoy" && dateFilter != "Esta semana" && dateFilter != "Este mes" && event.date == dateFilter)
                        ) &&
                event.name_event.contains(query, ignoreCase = true)
    }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

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
                IconButton(
                    onClick = {
                        //navController.navigate(BottomBarScreen.Home.route)
                        navController.navigate("home") {
                            // Establece `launchSingleTop` para evitar duplicados
                            launchSingleTop = true
                            // Establece `popUpTo` para limpiar el historial hasta `HomeScreen`
                            popUpTo("home") { inclusive = true }
                        }
                    })
                {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            })
            SearchBar(
                text = query,
                events = events,
                onQueryChange = { newQuery ->
                    query = newQuery
                    selectedCategory = null
                },
                onSearch = {
                    onSearch(query)
                },
                navController = navController
            )
            Spacer(modifier = Modifier.height(16.dp))

            FiltersTagView(
                bottomSheetState,
                coroutineScope,
                events,
                selectedCategory = categoryToFilter,
                selectedPrice = maxPrice?.toString() ?: "Todos",
                selectedType = typeFilter ?: "Todos",
                selectedDate = dateFilter ?: "Todos"
            )

            Text(
                text = "Resultados de la búsqueda:",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 14.dp, start = 8.dp),
                fontSize = 16.sp
            )

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


