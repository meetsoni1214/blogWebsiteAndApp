package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.androidapp.navigation.destinations.categoryRoute
import com.example.androidapp.navigation.destinations.detailsroute
import com.example.androidapp.navigation.destinations.homeRoute

@Composable
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        homeRoute(
            onCategorySelect = { category ->
                navController.navigate(Screen.Category.passCategory(category))
            },
            onPostClick = { postId ->
                navController.navigate(Screen.Details.passPostId(postId))
            }
        )
        categoryRoute(
            onBackPress = {navController.popBackStack()},
            onPostClick = {
                navController.navigate(Screen.Details.passPostId(it))
            }
        )
        detailsroute(
            onBackPres = {
                navController.popBackStack()
            }
        )
    }
}