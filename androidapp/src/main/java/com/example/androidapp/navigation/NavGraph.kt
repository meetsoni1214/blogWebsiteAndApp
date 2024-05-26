package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {

        }
        composable(route = Screen.Details.route) {

        }
        composable(route = Screen.Category.route) {

        }

    }
}