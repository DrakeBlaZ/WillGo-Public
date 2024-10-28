package com.example.willgo.graphs

import android.app.appsearch.SearchResults
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.willgo.data.Event
import com.example.willgo.view.screens.HomeScreen
import com.example.willgo.view.screens.MapScreen
import com.example.willgo.view.screens.ProfileScreen
import com.example.willgo.view.screens.SearchResultsScreen
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

@Composable
fun MainNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val events = remember { mutableStateOf(listOf<Event>()) }
    LaunchedEffect(Unit) {
        loadEventsFromSupabase(events)
    }
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route, route = Graph.MAIN) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(paddingValues = paddingValues, events.value, navController)
        }

        composable(route = BottomBarScreen.Location.route) {
            MapScreen()
        }

        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }

        composable(route = "searchResults/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val filteredEvents = events.value.filter { it.name_event.contains(query, ignoreCase = true) } // Filtrar eventos
            SearchResultsScreen(
                paddingValues,
                events = filteredEvents,
                initialQuery = query,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults/$newQuery")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults/$searchQuery")
                },
                navController
            )
        }
    }
}

suspend fun loadEventsFromSupabase(eventsState: MutableState<List<Event>>){

        try{
            val client = getClient()
            val supabaseResponse = client.postgrest["Evento"].select()
            val events = supabaseResponse.decodeList<Event>()
            Log.d("Supabase", "Eventos obtenidos: ${events.size}")
            eventsState.value = events
        } catch (e: Exception) {
            Log.e("Supabase", "Error al obtener eventos: ${e.message}")
        }
    }

private fun getClient(): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = "https://trpgyhwsghxnaakpoftt.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRycGd5aHdzZ2h4bmFha3BvZnR0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjgwMjgwNDcsImV4cCI6MjA0MzYwNDA0N30.IJthecg-DH9rwOob2XE6ANunb6IskxCbMAacducBVPE"
    ){
        install(Postgrest)
    }
}