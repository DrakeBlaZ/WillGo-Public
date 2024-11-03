package com.example.willgo.graphs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.willgo.view.sections.FiltersNavScreens.AllFilters
import com.example.willgo.view.sections.FiltersNavScreens.CategoriesNavScreen
import com.example.willgo.view.sections.FiltersNavScreens.DateNavScreen
import com.example.willgo.view.sections.FiltersNavScreens.PriceNavScreen
import com.example.willgo.view.sections.FiltersNavScreens.TypeNavScreen

sealed class FiltersScreen(val route: String){
    object Filters: FiltersScreen("filters_screen")
    object Categories: FiltersScreen("categories_screen")
    object Date: FiltersScreen("date_screen")
    object Hour: FiltersScreen("hour_screen")
    object Distance: FiltersScreen("distance_screen")
    object Price: FiltersScreen("price_screen")
    object Type: FiltersScreen("type_screen")
}

@Composable
fun FiltersNavGraph(navController: NavHostController){
    val onBack: () -> Unit = { navController.popBackStack() }
    val modifier = Modifier.fillMaxWidth().height(56.dp)
    NavHost(navController = navController, startDestination = FiltersScreen.Filters.route, route = Graph.MAIN) {
        composable(route = FiltersScreen.Filters.route) {
            AllFilters(navController)
        }

        composable(route = FiltersScreen.Categories.route) {
            CategoriesNavScreen(onBack = onBack, modifier)
        }

        composable(route = FiltersScreen.Date.route){
            DateNavScreen(onBack = onBack, modifier)
        }

        composable(route = FiltersScreen.Price.route) {
            PriceNavScreen(onBack = onBack, modifier)
        }

        composable(route = FiltersScreen.Type.route) {
            TypeNavScreen(onBack = onBack, modifier)
        }
    }
}
