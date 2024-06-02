package com.example.androidapp.navigation.destinations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.androidapp.model.Category
import com.example.androidapp.navigation.Screen
import com.example.androidapp.screens.home.HomeScreen
import com.example.androidapp.screens.home.HomeViewModel

fun NavGraphBuilder.homeRoute(
    onCategorySelect: (Category) -> Unit,
    onPostClick: (String) -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        var searchBarOpened by remember { mutableStateOf(false) }
        var searchBarActive by remember { mutableStateOf(false) }
        var query by remember { mutableStateOf("") }
        HomeScreen(
            posts = viewModel.allPosts.value,
            searchedPosts = viewModel.searchedPosts.value,
            searchBarOpened = searchBarOpened,
            searchQuery = query,
            searchActive = searchBarActive,
            onSearchQueryChange = { query = it },
            onActiveChanged = { searchBarActive = it },
            onSearched = viewModel::searchPostsByTitle,
            onCategorySelect = {
                onCategorySelect(it)
            },
            onPostClick = {
                onPostClick(it)
            },
            onSearchBarChanged = { opened ->
                searchBarOpened = opened
                if (!opened) {
                    query = ""
                    searchBarActive = false
                    viewModel.resetSearchedPosts()
                }
            }
        )
    }
}