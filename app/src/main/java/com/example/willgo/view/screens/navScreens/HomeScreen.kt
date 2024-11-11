package com.example.willgo.view.screens.navScreens

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
import androidx.navigation.NavHostController
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.view.screens.normalizeText
import com.example.willgo.view.sections.EventCard
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.FiltersPreview

@Composable
fun HomeScreen(paddingValues: PaddingValues, events: List<Event>, navController: NavHostController){
    //var filteredEvents by remember { mutableStateOf(events) }
    var query by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
        .background(Color.White)){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopBar()
            SearchBar(
                text = query,
                events = events,
                onQueryChange = { newQuery ->
                    query = normalizeText(newQuery)
                },
                onSearch = {
                    navController.navigate("searchResults/${normalizeText(query)}")
                },
                navController = navController
            )

            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
            )
            {
                items(1) {
                    CategorySection(title = "Actuación musical", events = getEventsByCategory(events, Category.Actuacion_musical), navController)
                    CategorySection(title = "Comedia", events = getEventsByCategory(events, Category.Comedia), navController)
                    CategorySection(title = "Cultura", events = getEventsByCategory(events, Category.Cultura), navController)
                    CategorySection(title = "Deporte", events = getEventsByCategory(events, Category.Deporte), navController)
                    CategorySection(title = "Discoteca", events = getEventsByCategory(events, Category.Discoteca), navController)
                    CategorySection(title = "Teatro", events = getEventsByCategory(events, Category.Teatro), navController)
                }
            }
        }
    }
}

@Composable
fun TopBar(navigationIcon: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Box(modifier = Modifier.align(Alignment.TopStart)){
            navigationIcon()
        }
        Text(
            text = "WILLGO",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .align(Alignment.TopEnd)

        ) {
            Image(
                imageVector =  Icons.Default.AccountCircle, contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun CategorySection(title: String, events: List<Event>, navController: NavHostController) {
    SectionTitle(title = title)
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            CommonEventCard(event = event, navController)  // Muestra cada evento en su tarjeta
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(text:String, events: List<Event>, onQueryChange: (String) -> Unit, onSearch: () -> Unit, navController: NavController){
    var query by remember { mutableStateOf(text) }
    var active by remember { mutableStateOf(false) }

    Log.d("Search", "Función de búsqueda inicializada")

    val searchBarPadding by animateDpAsState(targetValue = if (active) 0.dp else 16.dp, label = "")

    SearchBar(
        query = query,
        onQueryChange = { newQuery ->
            query = newQuery
            onQueryChange(normalizeText(newQuery))
            Log.d("SearchBar", "Texto cambiado: $text")
        },
        onSearch = {
            active = false
            onSearch() // Navegar a la pantalla de resultados
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text("Buscar evento")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (active && text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Close icon",
                    modifier = Modifier.clickable {
                        query = ""
                        onQueryChange("")
                    }
                )
            }
        },
        modifier = Modifier.padding(horizontal = searchBarPadding),
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
    ) {
        FiltersPreview(navController, null, events)
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
            .clickable {

            }
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                imageVector =  Icons.Default.ChevronRight, contentDescription = null
            )
        }
    }
}

@Composable
fun EventList(events: List<Event>, navController: NavHostController){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Log.d("EventList", "Mostrando ${events.size} eventos en la lista")
        items(events.size){
                index ->
            CommonEventCard(event = events[index], navController)
            Log.d("EventList", "Evento mostrado: ${events[index].name_event}")
        }
    }
}

@Composable
fun EventSection() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator()}
        items(6){
            EventCard()
        }
        item{VerticalSeparator()}

    }
}

fun getEventsByCategory(events: List<Event>, category: Category): List<Event> {
    return events.filter { it.category == category }
}

@Composable
fun VerticalSeparator(){
    Box(modifier = Modifier
        .height(164.dp)
        .width(4.dp))
}



