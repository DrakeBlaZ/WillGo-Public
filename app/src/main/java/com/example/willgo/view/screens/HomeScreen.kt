package com.example.willgo.view.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.willgo.view.sections.EventCard

@Composable
fun HomeScreen(paddingValues: PaddingValues){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
        .background(Color.White)){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TopBar()
            Search()
            Spacer(modifier = Modifier.height(12.dp))
            //HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            LazyColumn(modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
            )
            {
                items(1)  {
                    SectionTitle(title = "Popular")
                    Spacer(modifier = Modifier.height(16.dp))
                    PopularSection()
                    Spacer(Modifier.height(16.dp))
                    SectionTitle(title = "Conciertos")
                    Spacer(Modifier.height(16.dp))
                    ConcertsSection()
                    Spacer(Modifier.height(16.dp))
                    SectionTitle(title = "Deportes")
                    Spacer(Modifier.height(16.dp))
                    PopularSection()
                    Spacer(Modifier.height(16.dp))
                }

            }
        }
    }

}

@Composable
private fun SectionTitle(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                imageVector =  Icons.Default.ChevronRight, contentDescription = null
            )
        }
    }
}

@Composable
 private fun TopBar() {
    Box(
        modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = "WILLGO",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .align(Alignment.TopEnd)

        ) {
            Image(
                imageVector =  Icons.Default.AccountCircle, contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchBarPadding by animateDpAsState(targetValue = if (active) 0.dp else 16.dp, label = "")

    SearchBar(
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            active = false
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
        trailingIcon = if(active && text.isNotEmpty()){{
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close icon")
        }}else{@Composable {}},
        modifier = Modifier.padding( horizontal = searchBarPadding),
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp)
    ) { }
}


@Composable
private fun PopularSection() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator()}
        items(6){
            EventCard()
        }
        item{VerticalSeparator()}

    }
}

@Composable
private fun ConcertsSection() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator()}
        items(6){
        }
        item{VerticalSeparator()}

    }
}

@Composable
private fun VerticalSeparator(){
    Box(modifier = Modifier
        .height(164.dp)
        .width(4.dp))
}


