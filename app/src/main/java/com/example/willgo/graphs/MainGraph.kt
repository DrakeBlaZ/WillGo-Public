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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.view.screens.HomeScreen
import com.example.willgo.view.screens.MapScreen
import com.example.willgo.view.screens.ProfileScreen
import com.example.willgo.view.screens.SearchResultsScreen
import com.example.willgo.view.sections.FiltersNavScreens.AllFilters
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

    // Estado para almacenar la categoría seleccionada externamente
    val externalSelectedCategory = remember { mutableStateOf<Category?>(null) }

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
                initialCategory = null,
                maxPrice = null,
                externalSelectedCategory = externalSelectedCategory.value,
                typeFilter = null,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults/$newQuery")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults/$searchQuery")
                },
                navController
            )
        }

        composable(
            route = "searchResults?query={query}&category={category}",
            arguments = listOf(
                navArgument("query") { defaultValue = "" },
                navArgument("category") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val categoryName = backStackEntry.arguments?.getString("category")
            val category = Category.entries.firstOrNull { it.name == categoryName }

            val filteredEvents = events.value.filter { event ->
                (category == null || event.category == category) &&
                        event.name_event.contains(query, ignoreCase = true)
            }

            SearchResultsScreen(
                paddingValues = paddingValues,
                events = filteredEvents,
                initialQuery = query,
                initialCategory = category,
                maxPrice = null,
                externalSelectedCategory = externalSelectedCategory.value,
                typeFilter = null,
                dateFilter = null,
                onQueryChange = { newQuery ->
                    navController.navigate("searchResults?query=$newQuery&category=${category?.name ?: ""}")
                },
                onSearch = { searchQuery ->
                    navController.navigate("searchResults?query=$searchQuery&category=${category?.name ?: ""}")
                },
                navController = navController
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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
                events = events.value,
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