package com.example.willgo.view.screens.other

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.willgo.data.Request
import com.example.willgo.data.User.User
import com.example.willgo.data.WillGo.WillGo
import com.example.willgo.data.WillGo.WillGoItem
import com.example.willgo.view.screens.getClient
import com.example.willgo.view.screens.getUser
import com.example.willgo.view.screens.normalizeText
import com.example.willgo.view.sections.WillGo.WillGoUserItem
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WillGoScreen(
    idEvent: Long,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
) {
    var user = remember { mutableStateListOf<WillGoItem>() }
    var requestedUsers = remember { mutableStateListOf<WillGoItem>() }
    val selectedUsers = remember { mutableStateListOf<String>() }
    var selectedTab by remember { mutableStateOf(0) } // Controla el tab seleccionado
    val tabs = listOf("Usuarios", "Ya solicitados") // Títulos de los tabs

    // Cargar usuarios al iniciar la pantalla
    LaunchedEffect(Unit) {
        val result = getAloneUsersItem(idEvent)
        user.addAll(result.value)
        requestedUsers.addAll(getAloneUsersRequested(idEvent).value)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WillGo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(8.dp)) {
                Button(
                    onClick = {
                        // Remover los usuarios seleccionados
                        val temp = user.toList()
                        user.clear()
                        user.addAll(temp.filter { !selectedUsers.contains(it.nickname) })
                        println(user)
                        sendWillGoRequests(selectedUsers)
                        selectedUsers.clear()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    enabled = selectedUsers.isNotEmpty()
                ) {
                    Text(text = "Enviar solicitud")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    onSearch = {},
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
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent, // Fondo de la barra
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer, // Color del texto de las tabs
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary // Color del indicador seleccionado
                        )
                    }
                },
                divider = {
                    HorizontalDivider(color = MaterialTheme.colorScheme.secondary) // Línea divisoria
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> RecienteContent(user, selectedUsers)
                1 -> YaSolicitados(requestedUsers, selectedUsers)
            }
        }
    }
}

// Función para mostrar la lista de usuarios recientes
@Composable
fun RecienteContent(user: SnapshotStateList<WillGoItem>, selectedUsers: SnapshotStateList<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(user) { item ->
            WillGoUserItem(
                name = item.name!!,
                nickname = item.nickname,
                followers = item.followers!!,
                onToggleSelect = {
                    item.isSelected = !(item.isSelected)!!
                    if (item.isSelected == true) {
                        selectedUsers.add(item.nickname ?: "")
                        println(selectedUsers)
                    } else {
                        selectedUsers.remove(item.nickname)
                    }
                    item.isSelected!!
                },
                modifier = Modifier
            )
        }
    }
}

// Función para mostrar la lista de usuarios ya solicitados
@Composable
fun YaSolicitados(
    user: SnapshotStateList<WillGoItem>,
    selectedUsers: SnapshotStateList<String>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(user) { item ->
            WillGoUserItem(
                name = item.name!!,
                nickname = item.nickname,
                followers = item.followers!!,
                onToggleSelect = {
                    item.isSelected!!
                },
                modifier = Modifier
            )
        }
    }
}

    @Preview
    @Composable
    fun WillGoScreenPreview() {
        WillGoScreen(3, PaddingValues(0.dp), {})
    }


    fun sendWillGoRequests(selectedUsers: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            getClient().postgrest["Solicitudes"].insert(selectedUsers)

        }
    }

   suspend fun getAloneUsers(idEvent: Long): MutableState<List<WillGo>> {
       val user = getUser()
       val response = getClient()
           .postgrest["WillGo"]
           .select {
               filter {
                   and {
                       eq("id_event", idEvent)
                       neq("user", user.nickname)
                       eq("alone", true)
                   }
               }
           }
       return mutableStateOf(response.decodeList<WillGo>())
   }

    suspend fun getAloneUsersItem(idEvent: Long): MutableState<List<WillGoItem>> {
        val aloneUsersID = getAloneUsers(idEvent)
        val aloneUsers =
            mutableListOf<WillGoItem>()  // Lista que usaremos para cargar los datos

        withContext(Dispatchers.IO) {
            aloneUsersID.value.map {
                aloneUsers.add(getUser(it.user).toWillGoItem()) // Transformamos los usuarios
            }
        }

        val userState =
            mutableStateOf<List<WillGoItem>>(aloneUsers) // Regresamos un MutableState
        return userState
    }

suspend fun getAloneUsersRequested(idEvent: Long): MutableState<List<WillGoItem>> {
    val aloneUsersID = getAloneUsers(idEvent)
    val aloneUsers =
        mutableListOf<WillGoItem>()  // Lista que usaremos para cargar los datos
    val user = getUser()
    val getRequestedUsers = aloneUsersID.value.mapNotNull { getRequest(it.id_event, user.nickname).value }
    withContext(Dispatchers.IO) {
        getRequestedUsers.map {
            aloneUsers.add(
                getUserFromWillGoID(it.userRequested).toWillGoItem()) // Transformamos los usuarios
        }
    }

    val userState =
        mutableStateOf<List<WillGoItem>>(aloneUsers) // Regresamos un MutableState
    return userState
}

suspend fun getUserFromWillGoID(idWillGo: Long): User {
    val user = getUser()
    val response = getClient()
        .postgrest["WillGo"]
        .select(Columns.list("user")) {
            filter {
                and {
                    eq("id_event", idWillGo)
                }
            }
        }
    val userRequested = getUser(response.decodeSingle<String>())
    return userRequested
}

suspend fun getRequest(idWillGo: Long, userRequesting: String): MutableState<Request?> {
    val response = getClient()
        .postgrest["Solicitudes"]
        .select {
            filter {
                and {
                    eq("userRequested", idWillGo)
                    eq("userRequesting", userRequesting)
                }
            }
        }
    val request = response.decodeSingleOrNull<Request>() // Devuelve `null` si no hay resultados
    return mutableStateOf(request)
}