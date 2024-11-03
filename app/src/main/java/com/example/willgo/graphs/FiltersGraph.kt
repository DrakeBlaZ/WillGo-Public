package com.example.willgo.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.view.screens.HomeScreen
import com.example.willgo.view.screens.MapScreen
import com.example.willgo.view.screens.ProfileScreen
import com.example.willgo.view.screens.SearchResultsScreen
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
fun FiltersNavGraph(navController: NavHostController, events: List<Event>, paddingValues: PaddingValues){
    val onBack: () -> Unit = { navController.popBackStack() }
    val modifier = Modifier.fillMaxWidth().height(56.dp)

    // Variable para almacenar la categoría seleccionada externamente
    val externalSelectedCategory = remember { mutableStateOf<Category?>(null) }

    NavHost(navController = navController, startDestination = FiltersScreen.Filters.route, route = Graph.MAIN) {
        composable(route = FiltersScreen.Filters.route) {
            AllFilters(navController)
        }

        composable(route = FiltersScreen.Categories.route) {
            CategoriesNavScreen(
                onBack = onBack,
                modifier = modifier,
                onCategorySelected = { selectedCategory ->
                    externalSelectedCategory.value = selectedCategory
                    // Navega a SearchResultsScreen pasando la categoría seleccionada
                    navController.navigate("searchResults?externalSelectedCategory=${selectedCategory.name}")
                }
            )
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

        // Define el destino de SearchResultsScreen para recibir la categoría seleccionada
        composable(
            route = "searchResults?externalSelectedCategory={externalSelectedCategory}",
            arguments = listOf(navArgument("externalSelectedCategory") { defaultValue = "" })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("externalSelectedCategory")
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            // Llama a SearchResultsScreen pasando la categoría seleccionada como externalSelectedCategory
            SearchResultsScreen(
                paddingValues = PaddingValues(),
                events = events,
                initialQuery = "",
                externalSelectedCategory = externalCategory,
                onQueryChange = { newQuery ->
                    // Actualiza la ruta de búsqueda para que refleje el nuevo término de búsqueda
                    navController.navigate("searchResults?query=$newQuery&externalSelectedCategory=${externalCategory?.name ?: ""}")
                },
                onSearch = { searchQuery ->
                    // Ejecuta una búsqueda nueva y navega con la query y categoría actualizadas
                    navController.navigate("searchResults?query=$searchQuery&externalSelectedCategory=${externalCategory?.name ?: ""}")
                },
                navController = navController
            )
        }

        composable(route = "home") {
            HomeScreen(paddingValues = paddingValues, events, navController)
        }
    }
}
