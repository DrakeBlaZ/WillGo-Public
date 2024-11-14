package com.example.willgo.view.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.willgo.data.Comment
import com.example.willgo.data.Event
import com.example.willgo.data.User
import com.example.willgo.graphs.loadEventsFromSupabase
import com.example.willgo.view.screens.getCommentsForUser
import com.example.willgo.view.sections.CommonEventCard
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun FollowingScreen(paddingValues: PaddingValues, nickname: String, navController: NavHostController, onBack: () -> Unit){
    val following = remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        following.value = getFollowingForUser(nickname)
    }

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(following.value) { following ->
                FollowingItem(following, navController)
        }
    }

}

@Composable
fun FollowingItem(user: User, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ){
        Row(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "\"${user.nickname}\"",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

suspend fun getFollowingForUser(nickname: String): List<User> {
    val client = getClient()
    val supabaseResponse = client.postgrest["seguidores"].select{
        filter { eq("follower", nickname)}
    }
    return supabaseResponse.decodeList<User>()
}