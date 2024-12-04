package com.example.willgo.view.screens.other

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.willgo.data.WillGo.WillGo
import com.example.willgo.data.WillGo.WillGoItem
import com.example.willgo.view.screens.getClient
import com.example.willgo.view.screens.normalizeText
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WillGoManagerScreen(
    idEvent: Long,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    navHostController: NavHostController
) {
    // Listas mutables
    val user = remember { mutableStateListOf<WillGoItem>() }
    val requestedUsers = remember { mutableStateListOf<WillGoItem>() }

    // Listas filtradas para búsqueda
    val filteredUsers = remember { mutableStateListOf<WillGoItem>() }
    val filteredRequestedUsers = remember { mutableStateListOf<WillGoItem>() }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Usuarios", "Ya solicitados")

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchBarPadding by animateDpAsState(targetValue = if (active) 0.dp else 16.dp)
    // Función para filtrar usuarios
    fun filterUsers(query: String) {
        filteredUsers.clear()
        filteredRequestedUsers.clear()

        if (query.isNotEmpty()) {
            filteredUsers.addAll(user.filter { it.willGo.user.contains(query, ignoreCase = true) })
            filteredRequestedUsers.addAll(requestedUsers.filter { it.willGo.user.contains(query, ignoreCase = true) })
        } else {
            filteredUsers.addAll(user)
            filteredRequestedUsers.addAll(requestedUsers)
        }
    }

    // Obtener datos iniciales
    LaunchedEffect(Unit) {
        val result = getUsersNotRequested(idEvent)
        user.addAll(result.value)
        requestedUsers.addAll(getAloneUsersRequested(idEvent).value)

        // Aplicar el filtro inicial
        filterUsers(query)
    }


    Scaffold(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        topBar = {
            TopAppBar(
                title = { Text("WillGo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.height(56.dp)){

                // Barra de búsqueda
                DockedSearchBar(
                    query = query,
                    onQueryChange = { newQuery ->
                        query = normalizeText(newQuery)
                        filterUsers(query) // Filtra cuando cambia el texto
                    },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Buscar usuario") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon") },
                    trailingIcon = {
                        if (active && query.isNotEmpty()) {
                            Icon(imageVector = Icons.Default.Close,
                                contentDescription = "Close icon",
                                modifier = Modifier.clickable { query = "" })
                        }
                    },
                    modifier = Modifier.padding(horizontal = searchBarPadding),
                ){}
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido de acuerdo a la pestaña seleccionada
            when (selectedTab) {
                0 -> MyRequests(filteredUsers, requestedUsers, navHostController)
                1 -> OtherRequests(filteredRequestedUsers, user)
            }
        }
    }
}


// Función para mostrar la lista de usuarios recientes
@Composable
fun MyRequests(user: SnapshotStateList<WillGoItem>, selectedUsers: SnapshotStateList<WillGoItem>, navHostController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(user) { item ->

        }
    }
}

// Función para mostrar la lista de usuarios ya solicitados
@Composable
fun OtherRequests(
    requestedUsers: SnapshotStateList<WillGoItem>,
    selectedRequestedUsers: SnapshotStateList<WillGoItem>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(requestedUsers) { item ->

        }
    }
}


suspend fun getRequests(idEvent: Long, userRequesting: String):SnapshotStateList<Request>{
    val response = getClient().postgrest["Solicitudes"]
        .select(Columns.list("userRequesting")){
            filter {
                and{
                    eq("userRequesting", userRequesting)
                    eq("idEvent", idEvent)
                }
            }
        }
    val requests = response.decodeList<Request>()
    return requests.toMutableStateList()
}



@Preview
@Composable
fun WillGoManagerPreview() {
    WillGoManagerScreen(3, PaddingValues(0.dp), {}, navHostController = NavHostController(LocalContext.current))
}

