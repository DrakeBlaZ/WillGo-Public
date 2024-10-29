package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.willgo.view.sections.FilterRow

@Composable
fun PriceNavScreen(modifier: Modifier){
    Column(modifier = modifier){
        FilterRow(modifier = Modifier.fillMaxWidth().weight(1f), filterName = "Price", value = "Todos")
    }
}