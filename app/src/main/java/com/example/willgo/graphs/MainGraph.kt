package com.example.willgo.graphs

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.view.screens.navScreens.HomeScreen
import com.example.willgo.view.screens.navScreens.MapScreen
import com.example.willgo.view.screens.navScreens.ProfileScreen
import com.example.willgo.view.screens.other.CategoryScreen
import com.example.willgo.view.screens.other.DetailEventScreen
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

        composable(
            route = HomeScreenRoutes.Category.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "DEFAULT"
            val category = getCategory(categoryName)
            CategoryScreen(onBack = { navController.popBackStack() }, category = category, events.value, navController)
        }

        composable(
            route = HomeScreenRoutes.DetailEvent.route,
        ) {
            DetailEventScreen(onBack = { navController.popBackStack() }, events.value[0])
        }
    
    }
}


fun getCategory(categoryName: String): Category{
     return when(categoryName){
        "Actuacion musical" ->  Category.Actuacion_musical
        "Comedia" ->  Category.Comedia
        "Cultura" ->  Category.Cultura
        "Deporte" ->  Category.Deporte
        "Discoteca" ->  Category.Discoteca
        "Teatro" ->  Category.Teatro
        else ->  Category.Actuacion_musical
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

sealed class HomeScreenRoutes(val route: String){
    object Category: HomeScreenRoutes("Category_Section/{categoryName}")
    object DetailEvent: HomeScreenRoutes("Detail_Event")
}