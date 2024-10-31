package com.example.willgo.view.sections.FiltersNavScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.willgo.data.Category
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FilterValueRow
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateNavScreen(
    //onBack: () -> Unit, modifier: Modifier
){
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
            Text("Fecha")
            Spacer(modifier = Modifier.height(8.dp))
            FilterValueRow(modifier = Modifier, value = "Todos")
            //DatePicker()
        }
    }
}