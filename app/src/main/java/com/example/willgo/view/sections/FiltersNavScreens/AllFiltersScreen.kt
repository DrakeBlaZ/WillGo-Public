package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.willgo.graphs.FiltersScreen
import com.example.willgo.view.sections.FilterRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFilters(navController: NavController){
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
                    .height(56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            )
        }
    ) {

        Column(modifier = Modifier.padding(it)) {
            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable {  navController.navigate(FiltersScreen.Categories.route) }, filterName = "Categoria", value = "Todos")
            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable{ navController.navigate(FiltersScreen.Price.route) }, filterName = "Precio", value = "Todos")
            //FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable{ navController.navigate(FiltersScreen.Price.route) }, filterName = "Distancia", value = "Todos")
            //FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable{ navController.navigate(FiltersScreen.Hour.route) }, filterName = "Hora", value = "Todos")
            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable{ navController.navigate(FiltersScreen.Type.route) }, filterName = "Tipo de lugar", value = "Todos")
            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f).clickable{ navController.navigate(FiltersScreen.Date.route) }, filterName = "Fecha", value = "10/10/2023")
//            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Fecha", value = "10/10/2023")
//            FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Fecha", value = "10/10/2023")
        }
    }
}

@Composable
fun ResultFilterButton(modifier: Modifier){
    Box(modifier = modifier){
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(text = "Aplicar filtros")
        }
    }

}