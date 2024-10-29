package com.example.willgo.view.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.willgo.data.Category
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FiltersPreview(){
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    if (bottomSheetState.isVisible) {
        MyModalBottomSheet(
            onDismiss = { coroutineScope.launch { bottomSheetState.hide() } },
            sheetState = bottomSheetState,
            onBack = { coroutineScope.launch { bottomSheetState.hide() } },
            navHostController = NavHostController(LocalContext.current)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        FilterButton(onClick = {
            keyboard?.hide()
            coroutineScope.launch { bottomSheetState.expand() }}, modifier = Modifier.padding(start = 8.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Categorías",
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold)
        FilterGrid()
    }
}

@Preview
@Composable
fun FilterGrid(){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Category.entries.toTypedArray()) { category ->
            CategoryItem(category = category)
        }
    }
}


@Composable
fun CategoryItem(category: Category){
    ElevatedButton(
        onClick = {},
        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Gray)
    ){
        Text(text = category.name)
    }
}

