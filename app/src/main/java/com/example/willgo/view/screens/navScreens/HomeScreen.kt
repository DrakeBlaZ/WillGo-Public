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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.navigation.NavHostController
import com.example.willgo.data.Event
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.EventSection
import com.example.willgo.view.sections.SectionTitle

@Composable
fun HomeScreen(paddingValues: PaddingValues, events: List<Event>, navController: NavHostController){
    var filteredEvents by remember { mutableStateOf(events) }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
        .background(Color.White)){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopBar()
            Search(events) { query ->
                filteredEvents = if (query.isEmpty()) {
                    events
                } else {
                    events.filter {
                        it.name_event.contains(query, ignoreCase = true)
                    }
                }
                Log.d("Search", "Query de búsqueda: $query, eventos filtrados: ${filteredEvents.size}")
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
            )
            {
                items(1)  {
                    Spacer(Modifier.height(16.dp))
                    EventSection("Popular", navController)
                    Spacer(Modifier.height(16.dp))
                    EventSection("Conciertos", navController)
                    Spacer(Modifier.height(16.dp))
                    EventSection("Deportes", navController)
                    Spacer(Modifier.height(16.dp))

                    SectionTitle(title = "Resultados de la búsqueda", navController = navController)
                    Spacer(Modifier.height(16.dp))
                    EventList(filteredEvents, navController)
                    Spacer(Modifier.height(16.dp))
                }

            }
        }
    }

}

@Composable
 private fun TopBar() {
    Box(
        modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
    ) {

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(events: List<Event>, onQueryChange: (String) -> Unit){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Log.d("Search", "Función de búsqueda inicializada")

    val searchBarPadding by animateDpAsState(targetValue = if (active) 0.dp else 16.dp, label = "")

    SearchBar(
        query = text,
        onQueryChange = {query ->
            text = query
            onQueryChange(query)
            Log.d("SearchBar", "Texto cambiado: $text")
        },
        onSearch = {
            active = false
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
        trailingIcon = if(active && text.isNotEmpty()){{
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close icon",
                modifier = Modifier.clickable{
                    text = ""
                    onQueryChange("")
                })
        }}else{@Composable {}},
        modifier = Modifier.padding( horizontal = searchBarPadding),
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp)
    ) { }
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





