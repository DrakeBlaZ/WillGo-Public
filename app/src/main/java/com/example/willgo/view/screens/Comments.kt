package com.example.willgo.view.screens

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.screens.Profile
import com.example.willgo.data.Event
import com.example.willgo.data.Comment


@Composable
fun CommentsOnEvents(navController: NavController) {
    val comments = remember {
        mutableStateListOf<Comment>(
            Comment(1, "Usuario 1", "Este evento es increíble", 1, "2024-11-10"),
            Comment(2, "Usuario 2", "Muy esperado este evento", 1, "2024-11-11"),
            // Más comentarios aquí...
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        itemsIndexed(comments) { index, comment ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                // Tarjeta del evento que es clickeable
                CommonEventCard(event = Event(
                    id = comment.eventId,
                    description = "Descripción del evento",
                    name_event = "Nombre del evento",
                    date = "2024-11-12",
                    image = "url_de_imagen_evento",  // Este URL puede ser dinámico
                    duration = 2.0f,
                    price = 10.0f,
                    category = null,
                    location = null,
                    asistance = 1000,
                    type = "Concierto"
                ), modifier = Modifier.clickable {
                    // Acción al hacer clic en la tarjeta
                    navController.navigate("eventDataScreen/${comment.eventId}")
                })

                Spacer(modifier = Modifier.height(8.dp))

                // Detalles del comentario
                Column {
                    Text(
                        text = comment.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = comment.content,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Fecha: ${comment.date}",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
