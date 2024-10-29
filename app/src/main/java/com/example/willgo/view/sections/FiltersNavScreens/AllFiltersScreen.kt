package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.willgo.view.sections.FilterRow

@Composable
fun AllFilters(modifier: Modifier){
    Column(modifier = modifier) {
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Categoria", value = "Todos")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Precio", value = "Todos")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Distancia", value = "Todos")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Hora", value = "Todos")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Tipo de lugar", value = "Todos")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Fecha", value = "10/10/2023")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Fecha", value = "10/10/2023")
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Fecha", value = "10/10/2023")
    }
}