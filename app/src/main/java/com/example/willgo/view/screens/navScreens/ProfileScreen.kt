package com.example.willgo.view.screens.navScreens

import android.app.Dialog
import android.provider.ContactsContract.Profile
import android.text.style.BackgroundColorSpan
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.willgo.data.Event
import com.example.willgo.data.User
import com.example.willgo.view.screens.getCommentsForUser
import com.example.willgo.view.screens.getFollowersForUser
import com.example.willgo.view.screens.getFollowingForUser
import com.example.willgo.view.sections.CommonEventCard
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


//@Preview
@Composable
fun ProfileScreen(navController: NavHostController = rememberNavController(), paddingValues: PaddingValues, user: User){
    var coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(paddingValues)
    )
    {
        TopBar2()

        LazyColumn(
            modifier = Modifier

                .fillMaxSize()
                .background(Color.White)
        )
        {
            items(1){

                ProfilePic2()
                DataSection(name = user.name)
                Spacer(Modifier.height(16.dp))
                ButtonsSection(
                    onSeguirClick = {
                        coroutineScope.launch {
                            val userFollowing = getFollowingForUser(user.nickname)
                            navController.navigate("following/${user.nickname}")
                        }
                    },
                    onSeguidoresClik = {
                        coroutineScope.launch {
                            val userFollowing = getFollowersForUser(user.nickname)
                            navController.navigate("follower/${user.nickname}")
                        }
                    },
                    onComentariosClick = {
                        coroutineScope.launch {
                            val userComments = getCommentsForUser(user.nickname)
                            navController.navigate("comments/${user.nickname}")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                FollowsSection(user.followers, user.followed)
                Spacer(Modifier.height(16.dp))
                SectionTitle2(title = "Asistirá próximamente")
                Spacer(Modifier.height(16.dp))
                ConcertsSection2()
                Spacer(Modifier.height(16.dp))
                SectionTitle2(title = "Eventos asistidos")
                Spacer(Modifier.height(16.dp))
                PopularSection2()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DataSection(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        //  modifier = Modifier.padding(horizontal = 12.dp)
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(

            text = name,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            //     modifier = Modifier.align(Alignment.Center)
        )


    }
}

@Composable
private fun ButtonsSection(onSeguirClick: () -> Unit, onComentariosClick: () -> Unit, onSeguidoresClik:()->Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onSeguirClick) {
            Text(text = "Seguidos")
        }
        Button (onClick = onSeguidoresClik) {
            Text(text = "Seguidores")
        }
        Button(onClick = onComentariosClick) {
            Text(text = "Comentarios")
        }
    }
}

@Composable
private fun FollowsSection(number1: Int, number2: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

/*
        Column(modifier = Modifier
            //  .padding(paddingValues)
            .background(Color.White)
        )
        {
        }
*/
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = number1.toString())
            Text(text = "Seguidores")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = number2.toString())
            Text(text = "Seguidos")
        }

    }
}


@Composable
fun SectionTitle2(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
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
                imageVector =  Icons.Default.ChevronRight, contentDescription = null, Modifier.size(50.dp)

            )
        }
    }
}

@Composable
fun TopBar2() {
    Box(
        modifier = Modifier.padding(12.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = "Perfil",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePic2(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(12.dp)
            .fillMaxWidth()
    ) {


        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .align(Alignment.Center)
                .size(150.dp)

        ) {
            Image(
                imageVector =  Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(150.dp)
            )
        }
    }



}


@Composable
fun PopularSection2() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator2()}
        items(6){
            CommonEventCard(event = Event(
                id = 1,
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
            ), modifier = Modifier
                .height(164.dp)
                .width(164.dp))
        }
        item{VerticalSeparator2()}

    }
}

@Composable
fun ConcertsSection2() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator2()}
        items(6){
            CommonEventCard(event = Event(
                id = 1,
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
            ), modifier = Modifier
                .height(164.dp)
                .width(164.dp))
        }
        item{VerticalSeparator2()}

    }
}

@Composable
fun VerticalSeparator2(){
    Box(modifier = Modifier
        .height(164.dp)
        .width(4.dp))
}
