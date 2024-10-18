package com.example.willgo.view.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun EventCard(){
    Card(
        modifier = Modifier
            .background(Color.LightGray)
            .height(84.dp)
            .width(128.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Color.Red)
        ){
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
                    .align(alignment = Alignment.TopEnd)
                    .padding(4.dp),
                tint = Color.Black
            )
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
                    .padding(4.dp),
                tint = Color.Black
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Blue)
        ){
            Text(
                text = "Concierto AC/DC, 21/12/2024",
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier
                        .padding(3.dp)
            )
        }

    }
}