package com.example.willgo.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.willgo.data.Event
import com.example.willgo.data.User
import com.example.willgo.data.WillGo
import com.example.willgo.view.sections.VerticalSeparator
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EventDataScreen(event: Event, paddingValues: PaddingValues, onBack: () -> Unit){
    var willgo by remember{ mutableStateOf(false)}
    LaunchedEffect(Unit) {
        willgo = getWillGo(event)
    }
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()){
                AsyncImage(
                    model = event.image,
                    contentDescription = "Imagen del evento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(8.dp),
                    colors = IconButtonDefaults.iconButtonColors(Color.White))
                {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            }

        }
        item {
            Text(text = event.date.toString(), modifier = Modifier.padding(8.dp), color = Color.Blue)
            Text(
                text = event.name_event,
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            )
        }

        item{
            CharacteristicEvent("Tiempo", event.duration.toString(), modifier = Modifier.padding(start = 16.dp))
            CharacteristicEvent("Precio", event.price.toString(), modifier = Modifier.padding(start = 16.dp))
        }

        item {

            Text("Acerca de este evento",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp))
            event.description?.let {
                Text(
                    text = it,
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
                    )
            }
        }

        item{ HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp), thickness = 1.dp) }

        item {
            WillGo(willgo, event, coroutineScope) { newWillGo ->
                willgo = newWillGo
            }
        }

        item{ HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp), thickness = 1.dp) }

        item {
            event.location?.let {
                Text(
                    "Ubicación",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )

                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )
            }
        }

        item{
            event.email_contact?.let {
                Text("Contacta con nosotros",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp))

                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )
            }
        }

    }

}

@Composable
fun CharacteristicEvent(type: String, text: String, modifier: Modifier){
    Row(
        modifier = modifier
    ){
        val image: ImageVector
        val unit: String?
        if(type == "Precio"){
            image = Icons.Default.AttachMoney
            unit = " €"
        }
        else{
            image = Icons.Default.AvTimer
            unit = " Horas"
        }
        Image(imageVector = image, contentDescription = null, modifier = Modifier.size(36.dp))
        VerticalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 0.dp,)
        Text(text + unit,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun WillGo(willGo: Boolean, event: Event, coroutineScope: CoroutineScope, onWillGoChanged: (Boolean) -> Unit) {
    var attendants by remember { mutableIntStateOf(0) }
    LaunchedEffect(event) {attendants = getAttendants(event)}
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)){
            Text(text = "Asistirán(${attendants})")
            Row {
                repeat(attendants){
                    Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }
            }
        }
        VerticalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 0.dp,)

        Row(
            modifier = Modifier.align(Alignment.TopEnd)
                .height(48.dp),
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                            if (willGo) {
                                deleteWillGo(event)
                                attendants--
                            } else {
                                addWillGo(event)
                                attendants++
                            }
                            onWillGoChanged(!willGo)
                    }

                },
                modifier = Modifier
            ) {
                Text(if (willGo) "WillGo ✔" else "WillGo")
            }

            VerticalSeparator()

            Button(
                onClick = {},
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    Color.Gray)
            ){
                Text(text = "Voy solo")
            }
        }
    }
}


suspend fun getWillGo(event: Event): Boolean {
    val client = getClient()
    val userNickname = getUser().nickname
    val supabaseResponse = client.postgrest["WillGo"].select {
        filter {
            and {
                eq("id_event", event.id) // Filtro por ID de evento
                eq("users", userNickname) // Filtro por nickname del usuario
            }
        }
    }
    val data = supabaseResponse.decodeList<WillGo>()
    return data.isNotEmpty() // Devuelve true si hay un registro
}

suspend fun getAttendants(event: Event):Int{
    val client = getClient()
    val supabaseResponse = client.postgrest["WillGo"].select(){
        filter{eq("id_event", event.id)}
    }
    val data = supabaseResponse.decodeList<WillGo>()
    return data.size
}

suspend fun addWillGo(event: Event){
    val user = getUser()
    val willGo = user.nickname?.let { WillGo(event.id.toInt(), it) }
    val client = getClient()
    willGo?.let {
        client.postgrest["WillGo"].insert(willGo)

    }
}


suspend fun deleteWillGo(event: Event){
    val user = getUser()
    val client = getClient()
    client.postgrest["WillGo"].delete(){
        filter{
            and{
                eq("id_event", event.id)
                eq("users", user.nickname)
            }
        }
    }
}


private suspend fun getUser(): User {
    val client = getClient()
    val supabaseResponse = client.postgrest["Usuario"].select()
    val data = supabaseResponse.decodeList<User>()
    Log.e("supabase", data.toString())
    return data[0]

}

private suspend fun getData(){

    val client = getClient()
    val supabaseResponse = client.postgrest["Usuario"].select()
    val data = supabaseResponse.decodeList<User>()
    Log.e("supabase", data.toString())

}

public fun getClient(): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = "https://trpgyhwsghxnaakpoftt.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRycGd5aHdzZ2h4bmFha3BvZnR0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjgwMjgwNDcsImV4cCI6MjA0MzYwNDA0N30.IJthecg-DH9rwOob2XE6ANunb6IskxCbMAacducBVPE"
    ){
        install(Postgrest)
    }
}

