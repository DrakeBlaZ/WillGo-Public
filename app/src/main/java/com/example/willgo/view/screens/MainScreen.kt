package com.example.willgo.view.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.willgo.data.Event
import com.example.willgo.graphs.BottomBarScreen
import com.example.willgo.graphs.MainNavGraph
import com.example.willgo.data.User
import com.example.willgo.view.screens.navScreens.getWillgoForUser
import com.example.willgo.view.screens.other.SplashScreen

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()){

    val userEvents = remember { mutableStateOf(emptyList<Event>()) }
    val user = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        user.value = getUser()
    }

    LaunchedEffect(user.value) {
        user.value?.let { currentUser ->
            userEvents.value = getWillgoForUser(currentUser.nickname) // Carga eventos cuando se obtiene el usuario
        }
    }

    if (user.value == null) {
        SplashScreen(navController)
    } else {

        Scaffold(
            bottomBar = { NavBar(navController ) }
        )
        {
            MainNavGraph(navController = navController, paddingValues = it, user = user.value!!, userEvents = userEvents)
        }
    }
}

@Composable
fun RowScope.AddItems(screen: BottomBarScreen, currentDestination: NavDestination?, navController: NavController) {
    val isSearchResults = currentDestination?.route?.contains("searchResults") == true
    val isCategory = currentDestination?.route?.contains("Category_Section/{categoryName}") == true
    val isSelected = if (isSearchResults || isCategory) {
        screen == BottomBarScreen.Home // Seleccionar Home cuando estamos en searchResults
    } else {
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    NavigationBarItem(
        selected =  isSelected,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = { Icon(imageVector = screen.icon,
            contentDescription = "image"
        ) }
    )
}

@Composable
fun NavBar(navController: NavController){
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Location,
        BottomBarScreen.Profile)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any{it.route == currentDestination?.route} ||
            currentDestination?.route?.contains("searchResults" ) == true
            ||
            currentDestination?.route?.contains("Category_Section/{categoryName}") == true
    if(bottomBarDestination) {
        BottomAppBar {
            screens.forEach { screen ->
                AddItems(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }

}
