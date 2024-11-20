package com.example.willgo.view.screens.other

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.willgo.view.screens.normalizeText
import com.example.willgo.view.sections.FiltersPreview
import com.example.willgo.view.sections.WillGo.WillGoUserItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WillGoScreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WillGo") },
                navigationIcon = {
                    IconButton(onClick = { }){
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(8.dp)){
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(text = "Enviar solicitud")
                }
            }
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                var query by remember { mutableStateOf("") }
                var active by remember { mutableStateOf(false) }

                Log.d("Search", "Función de búsqueda inicializada")

                val searchBarPadding by animateDpAsState(
                    targetValue = if (active) 0.dp else 16.dp,
                    label = ""
                )

                SearchBar(
                    query = query,
                    onQueryChange = { newQuery ->
                        query = normalizeText(newQuery)
                    },
                    onSearch = {

                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = {
                        Text("Buscar evento")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                    },
                    trailingIcon = {
                        if (active && query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close icon",
                                modifier = Modifier.clickable {
                                    query = ""
                                }
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = searchBarPadding),
                    windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                ) {
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            )
            {
                items(5){
                    WillGoUserItem()
                }
            }

        }
    }

}