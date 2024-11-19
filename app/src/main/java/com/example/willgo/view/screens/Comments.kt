package com.example.willgo.view.screens

import android.util.Log
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.foundation.lazy.items  // Asegúrate de usar este items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.screens.Profile
import com.example.willgo.data.Event
import com.example.willgo.data.Repository
import com.example.willgo.data.Comment
import com.example.willgo.graphs.loadEventsFromSupabase
import com.example.willgo.view.screens.navScreens.getEventsByCategory
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun CommentsOnEvents(navController: NavController, nickname: String, paddingValues: PaddingValues, onBack: () -> Unit) {
    val comments = remember {
        mutableStateOf(listOf<Comment>())
    }
    val coroutineScope = rememberCoroutineScope()




    LaunchedEffect(nickname) {
        comments.value = getCommentsForUser(nickname)
    }

    val events = remember { mutableStateOf(listOf<Event>()) }

    LaunchedEffect(Unit) {
        loadEventsFromSupabase(events)
    }

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(comments.value) { comment ->
            val event = events.value.firstOrNull { it.id == comment.event_id }

            event?.let {
                CommentItem(comment, it, navController)
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, event: Event, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("eventDetail/${comment.event_id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Mostrar el comentario entre comillas
                Text(
                    text = "\"${comment.comment}\"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Mostrar el nombre del evento
                Text(
                    text = "en ${event.name_event}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Mostrar la fecha del comentario en formato reducido
                comment.created_at?.let {
                    val oldDate =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(it)
                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(oldDate)
                    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(oldDate)
                    Text(
                        text = "Publicado el: $date a las $time",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            // Mostrar la imagen del evento, si existe
            event.image?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

            }
        }
    }
}


suspend fun getCommentsForUser(nickname: String): List<Comment> {
    val client = getClient()
    val supabaseResponse = client.postgrest["comentario"].select{
        filter { eq("user_nickname", nickname)}
    }
    return supabaseResponse.decodeList<Comment>()
}
