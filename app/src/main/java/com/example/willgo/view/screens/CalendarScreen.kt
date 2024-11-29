package com.example.willgo.view.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.willgo.data.Event
import java.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.willgo.view.screens.navScreens.TopBar
import com.example.willgo.view.screens.navScreens.getWillgoForUser
import java.text.ParseException
import java.util.*

@Composable
fun CalendarScreen(
    userNickname: String,
    navController: NavController,
    paddingValues: PaddingValues
) {

    var selectedDate by remember{ mutableStateOf(Calendar.getInstance()) }
    val eventsByDate = remember { mutableStateOf<Map<String, List<Event>>>(emptyMap()) }
    val userEvents = remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(userNickname) {
        userEvents.value = getWillgoForUser(userNickname)
        eventsByDate.value = groupEventsByDate(userEvents.value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            TopBar(navigationIcon = {
                IconButton(
                    onClick = {
                        //navController.navigate(BottomBarScreen.Home.route)
                        navController.navigate("home") {
                            // Establece `launchSingleTop` para evitar duplicados
                            launchSingleTop = true
                            // Establece `popUpTo` para limpiar el historial hasta `HomeScreen`
                            popUpTo("home") { inclusive = true }
                        }
                    })
                {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            })

            //Título de la pantalla
            Text(
                text = "Calendario de Eventos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //Vista de los días de la semana
            WeekView(selectedDate){ newDate ->
                selectedDate = newDate
            }

            //Lista de eventos para la fecha seleccionada
            EventList(selectedDate, eventsByDate.value, navController)
        }
    }
}

@Composable
fun WeekView(selectedDate: Calendar, onDateSelected: (Calendar) -> Unit){

    // Estado para mantener la semana actual visible en la vista
    var currentWeek by remember { mutableStateOf(selectedDate.clone() as Calendar) }

    // Obtiene los días de la semana basados en la fecha seleccionada
    var weekDays by remember { mutableStateOf(getWeekDays(currentWeek)) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Texto para mostrar el mes y año actuales
        Text(
            text = "${
                currentWeek.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    Locale.getDefault()
                )
            } ${currentWeek.get(Calendar.YEAR)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Flecha hacia la izquierda
            IconButton(onClick = {
                currentWeek.add(Calendar.WEEK_OF_YEAR, -1) //Retrocede una semana
                weekDays = getWeekDays(currentWeek) // Actualiza los días de la semana
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retroceder una semana"
                )
            }

            // Usa LazyRow para mostrar los días de la semana en una fila desplazable horizontalmente
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Espaciado entre días
                modifier = Modifier.weight(1f) // Usa todo el ancho de la pantalla
            ) {
                // Recorre cada día de la semana
                items(weekDays.size) { index ->
                    val date = weekDays[index] // Obtén el día actual
                    // Comprueba si el día es el mismo que el seleccionado
                    val isSelected = isSameDay(date, selectedDate)

                    // Cada día se muestra como un círculo clicable
                    Box(
                        contentAlignment = Alignment.Center, // Centra el contenido (número del día)
                        modifier = Modifier
                            .size(50.dp) // Tamaño del círculo
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray, // Color según selección
                                shape = CircleShape // Forma circular
                            )
                            .clickable { onDateSelected(date) } // Cambia la fecha seleccionada al hacer clic
                    ) {
                        // Texto que muestra el número del día
                        Text(
                            text = date.get(Calendar.DAY_OF_MONTH).toString(),
                            color = if (isSelected) Color.White else Color.Black, // Color según selección
                            fontWeight = FontWeight.Bold, // Texto en negrita
                            fontSize = 16.sp // Tamaño de fuente
                        )
                    }
                }
            }

            // Flecha hacia la derecha
            IconButton(onClick = {
                currentWeek.add(Calendar.WEEK_OF_YEAR, 1) // Avanza una semana
                weekDays = getWeekDays(currentWeek) // Actualiza los días de la semana
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Avanzar semana"
                )
            }
        }
    }
}

@Composable
fun EventList(
    selectedDate: Calendar, // Fecha seleccionada
    eventsByDate: Map<String, List<Event>>, // Eventos agrupados por fecha
    navController: NavController // Controlador de navegación
) {
    // Formateador para convertir la fecha seleccionada en una clave para el mapa
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val selectedDateKey = dateFormatter.format(selectedDate.time) // Clave para la fecha seleccionada
    val eventsForSelectedDate = eventsByDate[selectedDateKey] ?: emptyList() // Obtiene los eventos para esa fecha

    Log.d("EventListDebug", "Selected Date Key: $selectedDateKey, Events: $eventsForSelectedDate")

    Spacer(modifier = Modifier.height(16.dp)) // Espacio entre la vista de días y los eventos

    if (eventsForSelectedDate.isEmpty()) {
        // Si no hay eventos para la fecha seleccionada, muestra un mensaje
        Text(
            text = "No hay eventos para esta fecha.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            color = Color.Gray
        )
    } else {
        // Si hay eventos, los muestra en una columna
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp), // Espaciado entre eventos
            modifier = Modifier.fillMaxWidth()
        ) {
            // Recorre cada evento y lo muestra como una tarjeta clicable
            eventsForSelectedDate.forEach { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth() // Cada tarjeta usa todo el ancho
                        .clickable { navController.navigate("eventDetail/${event.id}") }, // Navega a los detalles del evento
                ) {
                    // Contenido de la tarjeta
                    Row(
                        modifier = Modifier
                            .padding(16.dp) // Margen interior
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // Espaciado entre elementos
                        verticalAlignment = Alignment.CenterVertically // Centrado vertical
                    ) {
                        // Nombre del evento
                        Text(
                            text = event.name_event,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        // Fecha u hora del evento
                        Text(
                            text = event.date.orEmpty(),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

fun groupEventsByDate(events: List<Event>): Map<String, List<Event>> {
    val inputDateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato esperado de entrada
    val outputDateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato esperado de salida

    events.forEach { event ->
        Log.d("EventDebug", "Event: ${event.name_event}, Date: ${event.date}")
    }

    return events.groupBy { event ->
        try {
            event.date?.let {
                // Intenta analizar y reformatear la fecha
                val parsedDate = inputDateFormatter.parse(it)
                outputDateFormatter.format(parsedDate) // Devuelve la fecha formateada
            } ?: "" // Maneja el caso de fecha nula
        } catch (e: ParseException) {
            Log.e("DateParsing", "Error parsing date: ${event.date}. Expected format: dd/MM/yyyy", e)
            "" // Usa una clave vacía si hay un error
        }
    }.filterKeys { it.isNotEmpty() } // Filtra claves vacías
}


fun getWeekDays(baseDate: Calendar): List<Calendar> {
    // Ajusta el calendario al primer día de la semana
    val startOfWeek = baseDate.clone() as Calendar
    startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.firstDayOfWeek)

    // Genera una lista con los 7 días de la semana
    return (0..6).map {
        (startOfWeek.clone() as Calendar).apply {
            add(Calendar.DAY_OF_WEEK, it) // Avanza un día en cada iteración
        }
    }
}

fun isSameDay(date1: Calendar, date2: Calendar): Boolean {
    return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
            date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)
}

