package com.example.blogmultiplatform.pages

import androidx.compose.runtime.*
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.MainSection
import com.example.blogmultiplatform.sections.PostsSection
import com.example.blogmultiplatform.util.readLatestPosts
import com.example.blogmultiplatform.util.readMainPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch

@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        readMainPosts(
            onSuccess = {
                mainPosts = it
                        },
            onError = {mainPosts = ApiListResponse.Error(it.message.toString())}
        )
        readLatestPosts(
            skip = latestPostsToSkip,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    latestPosts.addAll(it.data)
                    latestPostsToSkip += POSTS_PER_PAGE
                    if (it.data.size >= POSTS_PER_PAGE) showMoreLatest = true
                }
            },
            onError = {}
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClosed = {
                    overflowOpened = false
                },
                content = { CategoryNavigationItems(vertical = true) }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpened = { overflowOpened = true }
        )
        MainSection(breakpoint = breakpoint, posts = mainPosts)
        PostsSection(
            breakpoint = breakpoint,
            posts = latestPosts,
            title = "Latest Posts",
            showMoreVisibility = showMoreLatest,
            onShowMore = {
               scope.launch {
                   readLatestPosts(
                       skip = latestPostsToSkip,
                       onSuccess = { response ->
                           if (response is ApiListResponse.Success) {
                               if (response.data.isNotEmpty()) {
                                   if (response.data.size < POSTS_PER_PAGE) {
                                       showMoreLatest = false
                                   }
                                   latestPosts.addAll(response.data)
                                   latestPostsToSkip += POSTS_PER_PAGE
                               } else {
                                   showMoreLatest = false
                               }
                           }
                       },
                       onError = {}
                   )
               }
            },
            onClick = {

            }
        )
    }
}
