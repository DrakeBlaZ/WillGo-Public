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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import com.example.willgo.data.User.UserResponse
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
    val selectedUsers = remember { mutableStateListOf<String>() }

    // Cargar usuarios al iniciar la pantalla
    LaunchedEffect(Unit) {
        val result = getAloneUsers(idEvent)
        user.addAll(result.value)
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
                        user.clear()
                        user.addAll(user.filter { !selectedUsers.contains(it.nickname) })
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
            HorizontalDivider()
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

    fun getRequests(idEvent: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            getClient().postgrest["Solicitudes"].select() {
                filter {
                    and {
                        eq("userRequested.id_event", idEvent)


                    }
                }
            }
        }
    }

    suspend fun getAloneUsers(idEvent: Long): MutableState<List<WillGoItem>> {
        val response = getClient()
            .postgrest["WillGo"]
            .select {
                filter {
                    and {
                        eq("id_event", idEvent)
                        neq("user", getUser().nickname)
                        eq("alone", true)
                    }
                }
            }
        val aloneUsersID = response.decodeList<WillGo>()
        val aloneUsers =
            mutableListOf<WillGoItem>()  // Lista que usaremos para cargar los datos

        withContext(Dispatchers.IO) {
            aloneUsersID.map {
                aloneUsers.add(getUser(it.user).toWillGoItem()) // Transformamos los usuarios
            }
        }

        val userState =
            mutableStateOf<List<WillGoItem>>(aloneUsers) // Regresamos un MutableState
        return userState
    }
