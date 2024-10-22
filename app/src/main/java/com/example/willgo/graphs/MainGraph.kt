package com.example.willgo.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.willgo.view.screens.HomeScreen
import com.example.willgo.view.screens.MapScreen
import com.example.willgo.view.screens.ProfileScreen

@Composable
fun MainNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route, route = Graph.MAIN) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(paddingValues = paddingValues)
        }

        composable(route = BottomBarScreen.Location.route) {
            MapScreen()
        }

        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }


    }
}