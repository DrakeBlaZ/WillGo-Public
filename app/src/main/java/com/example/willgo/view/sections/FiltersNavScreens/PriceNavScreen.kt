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
import com.example.willgo.data.Category
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FilterValueRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceNavScreen(onBack: () -> Unit, modifier: Modifier){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtros") },
                navigationIcon = { IconButton(onClick = onBack){ Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null) } }
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
            Text("Precio")
            Spacer(modifier = Modifier.height(8.dp))
            FilterValueRow(modifier = Modifier, value = "Todos", onClick = {})
            FilterValueRow(modifier = Modifier, value = "Gratis", onClick = {})
            FilterValueRow(modifier = Modifier, value = "5 euros", onClick = {})
            FilterValueRow(modifier = Modifier, value = "10 euros", onClick = {})
            FilterValueRow(modifier = Modifier, value = "20 euros", onClick = {})
            FilterValueRow(modifier = Modifier, value = "50 euros", onClick = {})
            FilterValueRow(modifier = Modifier, value = "100 euros", onClick = {})
            FilterValueRow(modifier = Modifier, value = "200 euros", onClick = {})
        }
    }
}