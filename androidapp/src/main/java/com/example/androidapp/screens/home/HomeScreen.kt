package com.example.androidapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.androidapp.components.NavigationDrawer
import com.example.androidapp.components.PostsCardView
import com.example.androidapp.model.Category
import com.example.androidapp.model.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: RequestState<List<Post>>,
    searchedPosts: RequestState<List<Post>>,
    searchBarOpened: Boolean,
    searchQuery: String,
    searchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchBarChanged:(Boolean) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onSearched: (String) -> Unit,
    onPostClick: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        drawerState = drawerState,
        onCategorySelect = {
            scope.launch {
                drawerState.close()
            }
            onCategorySelect(it) },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(text = "Blog")
                },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchBarChanged(true)
                            onActiveChanged(true)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                )
                if (searchBarOpened) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onSearch = onSearched,
                        active = searchActive,
                        onActiveChange = onActiveChanged,
                        placeholder = { Text("Search Posts By Title") },
                        leadingIcon = {
                            IconButton(onClick = { onSearchBarChanged(false) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    ) {
                        PostsCardView(
                            posts = searchedPosts,
                            topMargin = 12.dp,
                            onPostClick = onPostClick
                        )
                    }
                }
            },
            content =  {
                PostsCardView(
                    posts = posts,
                    hideMessage = true,
                    topMargin = it.calculateTopPadding(),
                    onPostClick = onPostClick
                )
            }
        )
    }
}