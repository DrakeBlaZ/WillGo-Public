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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.willgo.data.Category
import com.example.willgo.view.sections.FilterRow
import com.example.willgo.view.sections.FilterValueRow
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateNavScreen(
    onBack: () -> Unit, modifier: Modifier
){
    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
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
                    .height(56.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                onClick = {}
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text("Fecha")
            Spacer(modifier = Modifier.height(8.dp))
            FilterValueRow(modifier = Modifier, value = "Todos", onClick = {})
            FilterValueRow(modifier = Modifier, value = "Hoy", onClick = {})
            FilterValueRow(modifier = Modifier, value = "Esta semana", onClick = {})
            FilterValueRow(modifier = Modifier, value = "Este mes", onClick = {})
            FilterValueRow(modifier = Modifier.clickable { showDialog = true }, value = "Selecciona dia", onClick = {})
            if(showDialog){
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button (onClick = { showDialog = false }) { Text("Confirmar") }
                                    },
                ){
                    DatePicker(state = state)
                }
            }
        }
    }
}