package com.example.willgo.view.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.willgo.view.screens.NavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersTagView(sheetState: SheetState, coroutineScope: CoroutineScope){
    val keyboard = LocalSoftwareKeyboardController.current
    if (sheetState.isVisible) {
        MyModalBottomSheet(
            onDismiss = { coroutineScope.launch { sheetState.hide() } },
            sheetState = sheetState,
            onBack = { coroutineScope.launch { sheetState.hide() } },
        )
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(){
            FilterButton(onClick = {
                keyboard?.hide()
                coroutineScope.launch { sheetState.expand() }}, modifier = Modifier.padding(start = 8.dp))
        }
        items(5){
            FilterAddedCard("Concierto")
        }
    }
}

@Composable
fun FilterButton(onClick: () -> Unit, modifier: Modifier){
    Button(onClick = onClick, modifier = modifier) {
        Text("Filtros")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheet(onDismiss: () -> Unit, sheetState: SheetState, onBack: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        windowInsets = WindowInsets(bottom = 0.dp),
    ) {
        FilterPanel(onBack = onBack)
    }
}

@Composable
fun FilterAddedCard(filter: String){
    ElevatedButton(
        onClick = {},
        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Gray)
    ) {
        Text("Elevated con Icono")
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = Icons.Default.Close, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(onBack:() -> Unit){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filtros") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
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
}

@Composable
private fun ResultFilterButton(modifier: Modifier){
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

@Composable
fun FilterRow(modifier: Modifier, filterName: String, value: String){
    Box(modifier = modifier
        .drawBehind {
            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
        .clickable {  }
    ){
        Text(text = filterName, modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp))
        Text(text = value, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp))
    }
}