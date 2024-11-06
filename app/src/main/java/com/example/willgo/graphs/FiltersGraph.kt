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

    val externalSelectedCategory = remember { mutableStateOf<Category?>(null) }
    val maxPriceFilter = remember { mutableStateOf<Float?>(null) }
    val selectedTypeFilter = remember { mutableStateOf<String?>(null) }
    val selectedDateFilter = remember { mutableStateOf<String?>(null) }

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
                },
                navController = navController
            )
        }

        composable(route = FiltersScreen.Date.route){
            DateNavScreen(
                onBack = onBack,
                modifier,
                navController = navController,
                onDateSelected = { selectedDate ->
                    selectedDateFilter.value = selectedDate
                }
            )
        }

        composable(route = FiltersScreen.Price.route) {
            PriceNavScreen(
                onBack = onBack,
                modifier,
                navController,
                onPriceSelected = { maxPrice ->
                    maxPriceFilter.value = maxPrice
                }
            )
        }

        composable(route = FiltersScreen.Type.route) {
            TypeNavScreen(
                onBack = onBack,
                modifier,
                navController,
                onTypeSelected = { selectedType ->
                    selectedTypeFilter.value = selectedType
                }
            )
        }

        // Ruta para categoría, precio, tipo y fecha
        composable(
            route = "searchResults?maxPrice={maxPrice}&category={category}&type={type}&date={date}",
            arguments = listOf(
                navArgument("maxPrice") { defaultValue = "10000" },
                navArgument("category") { defaultValue = "" },
                navArgument("type") { defaultValue = "Todos" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull() ?: 10000f
            val categoryName = backStackEntry.arguments?.getString("category")
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"

            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = externalCategory,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = if (dateFilter == "Todos") null else dateFilter, // Añadido filtro de fecha
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&maxPrice=${maxPrice ?: ""}&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&maxPrice=${maxPrice ?: ""}&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para categoria, precio y tipo
        composable(
            route = "searchResults?maxPrice={maxPrice}&category={category}&type={type}",
            arguments = listOf(
                navArgument("maxPrice") { defaultValue = "10000" },
                navArgument("category") { defaultValue = "" },
                navArgument("type") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull() ?: 10000f
            val categoryName = backStackEntry.arguments?.getString("category")
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == null || maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = externalCategory,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    //navController.navigate("searchResults?query=$newQuery&maxPrice=${if (maxPrice != 10000f) maxPrice else ""}&category=${externalCategory?.name ?: ""}&type=${if (typeFilter != "Todos") typeFilter else ""}")
                    navController.navigate(
                        "searchResults?query=$newQuery" +
                                "&maxPrice=${maxPrice ?: ""}" +
                                "&category=${externalCategory?.name ?: ""}" +
                                "&type=${typeFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    //navController.navigate("searchResults?query=$searchQuery&maxPrice=${if (maxPrice != 10000f) maxPrice else ""}&category=${externalCategory?.name ?: ""}&type=${if (typeFilter != "Todos") typeFilter else ""}")
                    navController.navigate(
                        "searchResults?query=$searchQuery" +
                                "&maxPrice=${maxPrice ?: ""}" +
                                "&category=${externalCategory?.name ?: ""}" +
                                "&type=${typeFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        // Ruta para solo categoría
        composable(
            route = "searchResults?category={category}",
            arguments = listOf(navArgument("category") { defaultValue = "" })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                externalSelectedCategory = externalCategory,
                maxPrice = null,
                typeFilter = null,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults?query=$newQuery&category=${externalCategory?.name ?: ""}")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults?query=$searchQuery&category=${externalCategory?.name ?: ""}")
                },
                navController = navController
            )
        }

        // Ruta para solo precio
        composable(
            route = "searchResults?maxPrice={maxPrice}",
            arguments = listOf(navArgument("maxPrice") { defaultValue = "10000" })
        ) { backStackEntry ->
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull()

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = null,
                typeFilter = null,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults?query=$newQuery&maxPrice=${maxPrice ?: ""}")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults?query=$searchQuery&maxPrice=${maxPrice ?: ""}")
                },
                navController = navController
            )
        }

        // Ruta para solo tipo
        composable(
            route = "searchResults?type={type}",
            arguments = listOf(navArgument("type") { defaultValue = "Todos" })
        ) { backStackEntry ->
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = null,
                externalSelectedCategory = null,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults?query=$newQuery&type=${typeFilter ?: ""}")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults?query=$searchQuery&type=${typeFilter ?: ""}")
                },
                navController = navController
            )
        }

        //Ruta para categoria y fecha
        composable(
            route = "searchResults?category={category}&date={date}",
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                externalSelectedCategory = externalCategory,
                maxPrice = null,
                typeFilter = null,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&category=${externalCategory?.name ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&category=${externalCategory?.name ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        // Ruta para categoría y precio
        composable(
            route = "searchResults?category={category}&maxPrice={maxPrice}",
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("maxPrice") { defaultValue = "10000" }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull()
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = externalCategory,
                typeFilter = null,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&maxPrice=${maxPrice ?: ""}&category=${externalCategory?.name ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&maxPrice=${maxPrice ?: ""}&category=${externalCategory?.name ?: ""}"
                    )
                },
                navController = navController
            )
        }

        // Ruta para categoría y tipo
        composable(
            route = "searchResults?category={category}&type={type}",
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("type") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = null,
                externalSelectedCategory = externalCategory,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para precio y fecha
        composable(
            route = "searchResults?maxPrice={maxPrice}&date={date}",
            arguments = listOf(
                navArgument("maxPrice") { defaultValue = "10000" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull()
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = null,
                typeFilter = null,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&maxPrice=${maxPrice ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&maxPrice=${maxPrice ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para tipo y fecha
        composable(
            route = "searchResults?type={type}&date={date}",
            arguments = listOf(
                navArgument("type") { defaultValue = "Todos" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = null,
                externalSelectedCategory = null,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para categoria, tipo y fecha
        composable(
            route = "searchResults?category={category}&type={type}&date={date}",
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("type") { defaultValue = "Todos" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                externalSelectedCategory = externalCategory,
                maxPrice = null,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&category=${externalCategory?.name ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para precio, tipo y fecha
        composable(
            route = "searchResults?maxPrice={maxPrice}&type={type}&date={date}",
            arguments = listOf(
                navArgument("maxPrice") { defaultValue = "10000" },
                navArgument("type") { defaultValue = "Todos" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull()
            val typeFilter = backStackEntry.arguments?.getString("type") ?: "Todos"
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = null,
                typeFilter = if (typeFilter == "Todos") null else typeFilter,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&maxPrice=${maxPrice ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&maxPrice=${maxPrice ?: ""}&type=${typeFilter ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        //Ruta para categoria, precio y fecha
        composable(
            route = "searchResults?category={category}&maxPrice={maxPrice}&date={date}",
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("maxPrice") { defaultValue = "10000" },
                navArgument("date") { defaultValue = "Todos" }
            )
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category")
            val maxPrice = backStackEntry.arguments?.getString("maxPrice")?.toFloatOrNull()
            val dateFilter = backStackEntry.arguments?.getString("date") ?: "Todos"
            val externalCategory = categoryName?.takeIf { it.isNotBlank() }?.let { Category.valueOf(it) }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = events,
                initialQuery = "",
                maxPrice = if (maxPrice == 10000f) null else maxPrice,
                externalSelectedCategory = externalCategory,
                typeFilter = null,
                dateFilter = if (dateFilter == "Todos") null else dateFilter,
                onQueryChange = { newQuery ->
                    navController.navigate(
                        "searchResults?query=$newQuery&category=${externalCategory?.name ?: ""}&maxPrice=${maxPrice ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                onSearch = { searchQuery ->
                    navController.navigate(
                        "searchResults?query=$searchQuery&category=${externalCategory?.name ?: ""}&maxPrice=${maxPrice ?: ""}&date=${dateFilter ?: ""}"
                    )
                },
                navController = navController
            )
        }

        composable(route = "home") {
            HomeScreen(paddingValues = paddingValues, events, navController)
        }
    }
}
