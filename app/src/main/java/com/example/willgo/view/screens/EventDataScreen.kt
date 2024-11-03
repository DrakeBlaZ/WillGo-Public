package com.example.willgo.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.willgo.data.Event
import com.example.willgo.graphs.BottomBarScreen

@Composable
fun EventDataScreen(event: Event, paddingValues: PaddingValues, onBack: () -> Unit){
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()){
                AsyncImage(
                    model = event.image,
                    contentDescription = "Imagen del evento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(8.dp),
                    colors = IconButtonDefaults.iconButtonColors(Color.White))
                {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            }

        }
        item {
            Text(text = event.date.toString(), modifier = Modifier.padding(8.dp))
            Text(
                text = event.name_event,
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            )
        }

        item{
           //CharacteristicEvent("Tiempo", event.Duration.toString()))
            CharacteristicEvent("Precio", event.price.toString(), modifier = Modifier.padding(start = 16.dp))
        }

        item {

            Text("Acerca de este evento",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp))
            event.description?.let {
                Text(
                    text = it,
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
                    )
            }
        }

        item{ HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp), thickness = 1.dp) }

        item { WillGo() }

        item{ HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp), thickness = 1.dp) }

        item {
            event.location?.let {
                Text(
                    "Ubicación",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )

                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )
            }
        }

        item{
            event.email_contact?.let {
                Text("Contacta con nosotros",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp))

                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )
            }
        }

    }

}

@Composable
fun CharacteristicEvent(type: String, text: String, modifier: Modifier){
    Row(
        modifier = modifier
    ){
        val image: ImageVector
        var unit: String? = null
        if(type == "Precio"){
            image = Icons.Default.AttachMoney
            unit = " €"
        }
        else{
            image = Icons.Default.AvTimer
            unit = "horas"
        }
        Image(imageVector = image, contentDescription = null, modifier = Modifier.size(24.dp))
        VerticalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 0.dp,)
        Text(text + unit, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Preview
@Composable
fun WillGo(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)){
            Text(text = "Asistirán")
            Row {
                Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            }
        }
        VerticalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 0.dp,)

        Row(
            modifier = Modifier.align(Alignment.TopEnd)
                .height(48.dp),
        ) {
            Button(
                onClick = {},
                modifier = Modifier
            ) {
                Text("WillGo")
            }

            VerticalSeparator()

            Button(
                onClick = {},
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    Color.Gray)
            ){
                Text(text = "Voy solo")
            }
        }
    }
}

