package com.example.willgo.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SectionsNavGraph(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = BottomBarScreen.Home.route,
        route = Graph.MAIN){


    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController){

}

//sealed class DetailsScreen(val route: String){
//    object SECTION:
//}