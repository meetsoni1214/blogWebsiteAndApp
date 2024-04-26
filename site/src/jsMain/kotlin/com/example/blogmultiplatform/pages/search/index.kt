package com.example.blogmultiplatform.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.LoadingIndicator
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.Constants.QUERY_PARAM
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.PostsSection
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res
import com.example.blogmultiplatform.util.readLatestPosts
import com.example.blogmultiplatform.util.searchPostsByCategory
import com.example.blogmultiplatform.util.searchPostsByTitle
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement

@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    var overflowOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val searchedPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var postsToSkip by remember { mutableStateOf(0) }
    var showMorePosts by remember { mutableStateOf(false) }
    val hasCategoryParam = remember(key1 = context.route) {
        context.route.params.containsKey(CATEGORY_PARAM)
    }
    val hasQueryParam = remember(key1 = context.route) {
        context.route.params.containsKey(QUERY_PARAM)
    }
    val scope = rememberCoroutineScope()
    val value = remember(key1 = context.route) {
        if (hasCategoryParam) {
            context.route.params.getValue(CATEGORY_PARAM)
        } else if (hasQueryParam) {
            context.route.params.getValue(QUERY_PARAM)
        } else {
            ""
        }
    }

    LaunchedEffect(key1 = context.route) {
        apiResponse = ApiListResponse.Idle
        postsToSkip = 0
        showMorePosts = false
        (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = ""
        if (hasCategoryParam) {
            searchPostsByCategory(
                category = runCatching { Category.valueOf(value) }.getOrElse { Category.Programming },
                onError = {},
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                skip = postsToSkip
            )
        } else if (hasQueryParam) {
            searchPostsByTitle(
                query = value,
                onError = {},
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                skip = postsToSkip
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClosed = {
                    overflowOpened = false
                },
                content = { CategoryNavigationItems(
                    selectedCategory = if (hasCategoryParam)
                        runCatching { Category.valueOf(value) }.getOrElse { Category.Programming }
                    else null,
                    vertical = true
                ) }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpened = {
                overflowOpened = true
            },
            logo = Res.Image.logo,
            selectedCategory = if (hasCategoryParam)
                runCatching { Category.valueOf(value) }.getOrElse { Category.Programming }
            else null
        )
        if (apiResponse is ApiListResponse.Success) {
            if (hasCategoryParam) {
                SpanText(
                    text = value.ifEmpty { Category.Programming.name },
                    modifier = Modifier
                        .fontFamily(FONT_FAMILY)
                        .fontSize(36.px)
                        .margin(top = 100.px, bottom = 40.px)
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                )
            } else if (hasQueryParam) {
                SpanText(
                    text = "Search results for: ${value.ifEmpty { "" }}",
                    modifier = Modifier
                        .fontFamily(FONT_FAMILY)
                        .fontSize(36.px)
                        .margin(top = 100.px, bottom = 40.px)
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                )
            }
            PostsSection(
                breakpoint = breakpoint,
                posts = searchedPosts,
                showMoreVisibility = showMorePosts,
                onShowMore = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostsByCategory(
                                skip = postsToSkip,
                                category = runCatching { Category.valueOf(value) }.getOrElse { Category.Programming },
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POSTS_PER_PAGE) {
                                                showMorePosts = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = {}
                            )
                        } else if (hasQueryParam) {
                            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = value
                            searchPostsByTitle(
                                skip = postsToSkip,
                                query = value,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POSTS_PER_PAGE) {
                                                showMorePosts = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = {}
                            )
                        }
                    }
                },
                onClick = {}
            )
        } else {
            LoadingIndicator()
        }
    }
}