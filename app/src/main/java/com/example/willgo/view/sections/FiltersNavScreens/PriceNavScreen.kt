package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.willgo.data.Category
import com.example.willgo.graphs.FiltersScreen
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FilterValueRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceNavScreen(
    onBack: () -> Unit,
    modifier: Modifier,
    navController: NavController,
    onPriceSelected: (Float) -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtros") },
                navigationIcon = { IconButton(onClick = onBack){ Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null) } }
            )
        }
        /*bottomBar = {
            ResultFilterButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                onClick = {
                    navController.popBackStack(route = FiltersScreen.Filters.route, inclusive = false)
                }
            )
        }*/
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text("Precio")
            Spacer(modifier = Modifier.height(8.dp))

            /*val categoryParam = selectedCategory?.name ?: ""
            FilterValueRow(modifier = Modifier, value = "Todos", onClick = {
                navController.navigate("searchResults?maxPrice=10000&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "Gratis", onClick = {
                navController.navigate("searchResults?maxPrice=0&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "5 euros", onClick = {
                navController.navigate("searchResults?maxPrice=5&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "10 euros", onClick = {
                navController.navigate("searchResults?maxPrice=10&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "20 euros", onClick = {
                navController.navigate("searchResults?maxPrice=20&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "50 euros", onClick = {
                navController.navigate("searchResults?maxPrice=50&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "100 euros", onClick = {
                navController.navigate("searchResults?maxPrice=100&category=$categoryParam")
            })
            FilterValueRow(modifier = Modifier, value = "200 euros", onClick = {
                navController.navigate("searchResults?maxPrice=200&category=$categoryParam")
            })*/

            /*val prices = listOf("Todos", "Gratis", "5 euros", "10 euros", "20 euros", "50 euros", "100 euros", "200 euros")
            prices.forEach { price ->
                FilterValueRow(
                    modifier = Modifier,
                    value = price,
                    onClick = {
                        navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", price)
                        navController.popBackStack() // Vuelve a `AllFiltersScreen`
                    }
                )
            }*/

            FilterValueRow(modifier = Modifier, value = "Todos", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "Todos")
                navController.popBackStack() // Regresar a AllFiltersScreen
            })
            FilterValueRow(modifier = Modifier, value = "Gratis", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "Gratis")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "5 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "5 euros")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "10 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "10 euros")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "20 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "20 euros")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "50 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "50 euros")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "100 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "100 euros")
                navController.popBackStack()
            })
            FilterValueRow(modifier = Modifier, value = "200 euros", onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedPrice", "200 euros")
                navController.popBackStack()
            })
        }
    }
}