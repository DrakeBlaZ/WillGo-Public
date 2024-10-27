package com.example.willgo.view.screens.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.willgo.data.Event

@Composable
fun DetailEventScreen(onBack: () -> Unit, event: Event){

    Scaffold(
        topBar = {TopBar(event.name_event, onBack)},
    ){
        Box(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding())) {
            Text(text = event.name_event, modifier = Modifier.align(Alignment.Center))
        }
    }

}