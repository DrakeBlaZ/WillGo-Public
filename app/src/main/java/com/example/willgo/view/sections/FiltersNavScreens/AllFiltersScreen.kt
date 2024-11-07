package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.graphs.FiltersScreen
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FiltersTagView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFilters(navController: NavController, navControllerMain: NavController, events: List<Event>) {
    // Estado para almacenar las selecciones
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedPrice by remember { mutableStateOf("Todos") }
    var selectedType by remember { mutableStateOf("Todos") }
    var selectedDate by remember { mutableStateOf("Todos") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtros") }
            )
        },
        bottomBar = {
            ResultFilterButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                onClick = {
                    // Navegar a `SearchResultsScreen` con los filtros seleccionados
                    val categoryParam = selectedCategory?.name ?: ""
                    val maxPriceParam = when (selectedPrice) {
                        "Todos" -> "10000"
                        "Gratis" -> "0"
                        else -> selectedPrice.removeSuffix(" euros")
                    }
                    val typeParam = selectedType.takeIf { it != "Todos" } ?: ""
                    val dateParam = selectedDate.takeIf { it != "Todos" } ?: ""


                    //navController.navigate("searchResults?maxPrice=${if (maxPriceParam != "10000") maxPriceParam else ""}&category=${categoryParam.ifEmpty { "" }}&type=${if (typeParam != "Todos") typeParam else ""}")

                    val route = when {
                        categoryParam.isNotEmpty() && maxPriceParam != "10000" && typeParam.isNotEmpty() && dateParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&maxPrice=$maxPriceParam&type=$typeParam&date=$dateParam"
                        categoryParam.isNotEmpty() && maxPriceParam != "10000" && typeParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&maxPrice=$maxPriceParam&type=$typeParam"
                        categoryParam.isNotEmpty() && maxPriceParam != "10000" && dateParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&maxPrice=$maxPriceParam&date=$dateParam"
                        categoryParam.isNotEmpty() && typeParam.isNotEmpty() && dateParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&type=$typeParam&date=$dateParam"
                        maxPriceParam != "10000" && typeParam.isNotEmpty() && dateParam.isNotEmpty() ->
                            "searchResults?maxPrice=$maxPriceParam&type=$typeParam&date=$dateParam"
                        categoryParam.isNotEmpty() && maxPriceParam != "10000" ->
                            "searchResults?category=$categoryParam&maxPrice=$maxPriceParam"
                        categoryParam.isNotEmpty() && typeParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&type=$typeParam"
                        categoryParam.isNotEmpty() && dateParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam&date=$dateParam"
                        maxPriceParam != "10000" && typeParam.isNotEmpty() ->
                            "searchResults?maxPrice=$maxPriceParam&type=$typeParam"
                        maxPriceParam != "10000" && dateParam.isNotEmpty() ->
                            "searchResults?maxPrice=$maxPriceParam&date=$dateParam"
                        typeParam.isNotEmpty() && dateParam.isNotEmpty() ->
                            "searchResults?type=$typeParam&date=$dateParam"
                        categoryParam.isNotEmpty() ->
                            "searchResults?category=$categoryParam"
                        maxPriceParam != "10000" ->
                            "searchResults?maxPrice=$maxPriceParam"
                        typeParam.isNotEmpty() ->
                            "searchResults?type=$typeParam"
                        dateParam.isNotEmpty() ->
                            "searchResults?date=$dateParam"
                        else -> "searchResults"
                    }
                    navControllerMain.navigate(route)
                }
            )
        }
    ) {

        Column(modifier = Modifier.padding(it)) {

            // Llamada a FiltersTagView para mostrar los filtros seleccionados
            FiltersTagView(
                selectedCategory = selectedCategory,
                selectedPrice = selectedPrice,
                selectedType = selectedType,
                selectedDate = selectedDate,
                onRemoveCategory = { selectedCategory = null },
                onRemovePrice = { selectedPrice = "Todos" },
                onRemoveType = { selectedType = "Todos" },
                onRemoveDate = { selectedDate = "Todos" },
            )

            Spacer(modifier = Modifier.height(8.dp))

            //Filtro de categoria
            FilterRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        // Navegar a `CategoriesNavScreen` y actualizar la selección de categoría
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedCategory", selectedCategory)
                        navController.navigate(FiltersScreen.Categories.route)
                    },
                filterName = "Categoria",
                value = selectedCategory?.name ?: "Todos"
            )

            //Filtro de precio
            FilterRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        // Navegar a `PriceNavScreen` y actualizar la selección de precio
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedPrice", selectedPrice)
                        navController.navigate(FiltersScreen.Price.route)
                    },
                filterName = "Precio",
                value = selectedPrice
            )

            //Filtro de tipo de lugar
            FilterRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedType", selectedType)
                        navController.navigate(FiltersScreen.Type.route)
                    },
                filterName = "Tipo de lugar",
                value = selectedType
            )

            //Filtro de fecha
            FilterRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        navController.navigate(FiltersScreen.Date.route)
                },
                filterName = "Fecha",
                value = selectedDate
            )
        }
    }

    // Recuperar las selecciones al regresar de las pantallas de selección
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Category>("selectedCategory")
        ?.observe(navController.currentBackStackEntry!!) { selectedCategory = it }

    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedPrice")
        ?.observe(navController.currentBackStackEntry!!) { selectedPrice = it }

    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedType")
        ?.observe(navController.currentBackStackEntry!!) { selectedType = it }

    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedDate")
        ?.observe(navController.currentBackStackEntry!!) { selectedDate = it }
}

@Composable
fun ResultFilterButton(modifier: Modifier, onClick: () -> Unit){
    Box(modifier = modifier){
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(text = "Aplicar filtros")
        }
    }
}