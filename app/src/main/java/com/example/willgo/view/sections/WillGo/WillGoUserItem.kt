package com.example.willgo.view.sections.WillGo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.willgo.data.User.UserResponse
import com.example.willgo.data.WillGo.WillGoItem

@Composable
fun WillGoUserItem(
    nickname: String,
    followers: Int,
    onToggleSelect: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    var isSelected = remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable { isSelected.value = onToggleSelect() }
            .background(if (isSelected.value) Color.LightGray else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(start = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = if (isSelected.value) Color.Blue else Color.Gray, // MODIFICADO,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = nickname,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .wrapContentSize()
                .padding(end = 16.dp)
            ,
            contentAlignment = Alignment.Center
        ){
               Icon(imageVector = Icons.Rounded.RemoveRedEye,
                   contentDescription = "Selected",
                   modifier = Modifier.size(36.dp)
                       .clickable {  },
                   tint = if (isSelected.value) Color.Blue else Color.Gray)

            }

    }

}

@Preview
@Composable
fun WillGoUserItemPreview() {
    WillGoUserItem("Paco Camarasa", 15, onToggleSelect = { true })
}