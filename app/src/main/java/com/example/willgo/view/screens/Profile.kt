package com.example.willgo.view.screens

import android.provider.ContactsContract.Profile
import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

@Preview
@Composable
fun Profile(/*paddingValues: PaddingValues, User: user*/){
    Column(modifier = Modifier
      //  .padding(paddingValues)
        .fillMaxSize()
        .background(Color.White)
    )
    {
        TopBar2()
        ProfilePic2()
        DataSection(name = "Nombre Apellido" /*user.name*/)
        Spacer(Modifier.height(16.dp))
        ButtonsSection(onSeguirClick = { }, onComentariosClick = {})
        Spacer(modifier = Modifier.height(16.dp))
        FollowsSection(12,12)
        Spacer(Modifier.height(16.dp))
        SectionTitle2(title = "Asistirá próximamente")
        Spacer(Modifier.height(16.dp))
        ConcertsSection2()
        Spacer(Modifier.height(16.dp))
        SectionTitle2(title = "Eventos asistidos")
        Spacer(Modifier.height(16.dp))
        PopularSection2()
        Spacer(Modifier.height(16.dp))

    }
}

@Composable
private fun DataSection(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
      //  modifier = Modifier.padding(horizontal = 12.dp)
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(

            text = name,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
       //     modifier = Modifier.align(Alignment.Center)
        )


    }
}

@Composable
private fun ButtonsSection(onSeguirClick: () -> Unit, onComentariosClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onSeguirClick) {
            Text(text = "Seguir")
        }
        Button(onClick = onComentariosClick) {
            Text(text = "Comentarios")

        }
    }
}

@Composable
private fun onComentariosClick(){
    val dialog = Dialog(this)

    val dialogLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(50, 50, 50, 50)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val closeButton = ImageButton(this).apply {
        setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
        }
        setOnClickListener {
            dialog.dismiss()
        }
    }

    val titleText = TextView(this).apply {
        text = "Comentarios"
        textSize = 18f
        setPadding(0, 0, 0, 20)
    }

    // Comentarios de ejemplo
    val comment1 = TextView(this).apply {
        text = "Comentario de ejemplo 1"
    }

    val comment2 = TextView(this).apply {
        text = "Comentario de ejemplo 2"
    }

    dialogLayout.addView(closeButton)
    dialogLayout.addView(titleText)
    dialogLayout.addView(comment1)
    dialogLayout.addView(comment2)

    dialog.setContentView(dialogLayout)
    dialog.show()



}


@Composable
private fun FollowsSection(number1: Int, number2: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = number1.toString())
            Text(text = "Seguidores")
        }

        // Segunda columna
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = number2.toString())
            Text(text = "Seguidos")
        }

      }
}


@Composable
fun SectionTitle2(title: String) {
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
                imageVector =  Icons.Default.ChevronRight, contentDescription = null, Modifier.size(50.dp)

            )
        }
    }
}

@Composable
fun TopBar2() {
    Box(
        modifier = Modifier.padding(12.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = "Perfil",
            color = Color.Black,
            fontSize = 18.sp,
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
                imageVector =  Icons.Default.AccountCircle, contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePic2(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(12.dp)
            .fillMaxWidth()
    ) {


        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .align(Alignment.Center)
                .size(150.dp)

        ) {
            Image(
                imageVector =  Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(150.dp)
            )
        }
    }



}


@Composable
fun PopularSection2() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator2()}
        items(6){
        }
        item{VerticalSeparator2()}

    }
}

@Composable
fun ConcertsSection2() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item{VerticalSeparator2()}
        items(6){
        }
        item{VerticalSeparator2()}

    }
}

@Composable
fun VerticalSeparator2(){
    Box(modifier = Modifier
        .height(164.dp)
        .width(4.dp))
}