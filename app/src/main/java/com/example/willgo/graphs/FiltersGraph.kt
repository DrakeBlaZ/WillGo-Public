package com.example.willgo.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

sealed class FiltersScreen(val route: String){
    object Categories: FiltersScreen("categories_screen")
    object Date: FiltersScreen("date_screen")
    object Hour: FiltersScreen("hour_screen")
    object Distance: FiltersScreen("distance_screen")
    object Price: FiltersScreen("price_screen")
    object Type: FiltersScreen("type_screen")
}

@Composable
fun FiltersNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route, route = Graph.MAIN) {
        composable(route = FiltersScreen.Categories.route) {

        }

        composable(route = FiltersScreen.Date.route){

        }

        composable(route = FiltersScreen.Hour.route) {

        }

        composable(route = FiltersScreen.Distance.route) {

        }

        composable(route = FiltersScreen.Price.route) {

        }

        composable(route = FiltersScreen.Type.route) {

        }
    }
}
