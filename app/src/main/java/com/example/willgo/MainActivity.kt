package com.example.willgo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.data.User
import com.example.willgo.retrofit.RetrofitClient
import com.example.willgo.ui.theme.WillGoTheme
import com.example.willgo.view.screens.HomeScreen
import com.example.willgo.view.screens.Profile
import com.example.willgo.view.screens.Comments
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        enableEdgeToEdge()
        setContent {
            WillGoTheme {
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.background
               ) {
                    Main()
               }
            }
        }
    }


    private fun getData(){
        lifecycleScope.launch{
            val client = getClient()
            val supabaseResponse = client.postgrest["Usuario"].select()
            val data = supabaseResponse.decodeList<User>()
            Log.e("supabase", data.toString())
        }
    }

    private fun getClient(): SupabaseClient{
        return createSupabaseClient(
            supabaseUrl = "https://trpgyhwsghxnaakpoftt.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRycGd5aHdzZ2h4bmFha3BvZnR0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjgwMjgwNDcsImV4cCI6MjA0MzYwNDA0N30.IJthecg-DH9rwOob2XE6ANunb6IskxCbMAacducBVPE"
        ){
            install(Postgrest)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Main() {
    /* PARA PROFILE
    Scaffold(
        bottomBar = {NavBar()},
    ){
        Profile(it)
    }*/

    // PARA COMMENTS
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavBar() },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "profile", // Ruta inicial
            Modifier.padding(paddingValues)
        ) {
            composable("profile") { Profile(navController, paddingValues) }
            composable("comments") { Comments(navController) }
        }
    }

}

@Composable
fun NavBar(){
    BottomAppBar {
        NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = { Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "home"
        ) })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = { Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "favorites"
        ) })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = { Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "location"
        ) })
        NavigationBarItem(selected = false, onClick = { /*TODO*/ }, icon = { Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "profile"
        ) })
    }

}