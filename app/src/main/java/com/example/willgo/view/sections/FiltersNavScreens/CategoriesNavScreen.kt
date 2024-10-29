package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.willgo.data.Category
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FilterValueRow

@Composable
fun CategoriesNavScreen(modifier: Modifier){
    Column(modifier = modifier) {
        for(category: Category in Category.entries){
            FilterValueRow( modifier = Modifier.fillMaxWidth().height(24.dp),value = "Todos")
        }
    }
}