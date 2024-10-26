package com.example.willgo.view.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventSection(title: String) {
    SectionTitle(title = title)
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{ VerticalSeparator() }
        items(6){
            EventCard()
        }
        item{ VerticalSeparator() }

    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp)
            .clickable {

            }
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
private fun VerticalSeparator(){
    Box(modifier = Modifier
        .height(164.dp)
        .width(4.dp))
}