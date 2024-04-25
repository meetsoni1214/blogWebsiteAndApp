package com.example.blogmultiplatform.pages

import androidx.compose.runtime.*
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.MainSection
import com.example.blogmultiplatform.sections.NewsletterContent
import com.example.blogmultiplatform.sections.NewsletterSection
import com.example.blogmultiplatform.sections.PostsSection
import com.example.blogmultiplatform.sections.SponsoredPosts
import com.example.blogmultiplatform.sections.SponsoredPostsSection
import com.example.blogmultiplatform.util.readLatestPosts
import com.example.blogmultiplatform.util.readMainPosts
import com.example.blogmultiplatform.util.readPopularPosts
import com.example.blogmultiplatform.util.readSponsoredPosts
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
    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }
    val popularPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var popularPostsToSkip by remember { mutableStateOf(0) }
    var showMorePopular by remember { mutableStateOf(false) }

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
        readSponsoredPosts(
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    sponsoredPosts.addAll(it.data)
                }
            },
            onError = {}
        )
        readPopularPosts(
            skip = popularPostsToSkip,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    popularPosts.addAll(it.data)
                    popularPostsToSkip += POSTS_PER_PAGE
                    if (it.data.size >= POSTS_PER_PAGE) showMorePopular = true
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
        SponsoredPostsSection(
            posts = sponsoredPosts,
            breakpoint = breakpoint,
            onClick = {},
        )
        PostsSection(
            breakpoint = breakpoint,
            posts = popularPosts,
            title = "Popular Posts",
            showMoreVisibility = showMorePopular,
            onShowMore = {
                scope.launch {
                    readPopularPosts(
                        skip = popularPostsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < POSTS_PER_PAGE) {
                                        showMorePopular = false
                                    }
                                    popularPosts.addAll(response.data)
                                    popularPostsToSkip += POSTS_PER_PAGE
                                } else {
                                    showMorePopular = false
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
        NewsletterSection(breakpoint = breakpoint)
    }
}
