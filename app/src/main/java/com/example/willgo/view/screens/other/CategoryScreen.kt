package com.example.willgo.view.screens.other

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.willgo.data.Category
import com.example.willgo.data.Event
import com.example.willgo.view.sections.CommonEventCard
import com.example.willgo.view.sections.EventCard

@Composable
fun CategoryScreen(onBack: () -> Unit, category: Category, events: List<Event>){
    Scaffold(
        topBar = {TopBar(category.toString(), onBack)},
    ){
        CategorySection(events.filter { it.category == category }, it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(categoryName: String, onBack: () -> Unit){
    TopAppBar(title = {  Text(categoryName.uppercase().replace("_", " "))},
        navigationIcon = {
            IconButton(onClick = onBack){ Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ArrowBack")}
        }
    )
}

@Composable
fun CategorySection(events: List<Event>, paddingValues: PaddingValues){
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues.calculateTopPadding())
        ){
        items(count = events.size){
            event -> CommonEventCard(events[event])
        }
    }
}