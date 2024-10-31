package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.willgo.view.sections.FilterRow

@Composable
fun PriceNavScreen(onBack: () -> Unit, modifier: Modifier){
    Column(modifier = Modifier.clickable {  }){
        FilterRow(modifier = modifier, filterName = "Price", value = "Todos")
    }
}