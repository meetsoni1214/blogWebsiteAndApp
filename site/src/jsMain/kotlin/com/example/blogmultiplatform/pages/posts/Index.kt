package com.example.blogmultiplatform.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants.SHOW_SECTIONS_PARAM
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.ErrorView
import com.example.blogmultiplatform.components.LoadingIndicator
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.sections.FooterSection
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.Res
import com.example.blogmultiplatform.util.parseDateString
import com.example.blogmultiplatform.util.readSelectedPosts
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

@Page(routeOverride = "post")
@Composable
fun postPage() {
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var showSections by remember { mutableStateOf(true) }
    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }
    LaunchedEffect(key1 = context.route) {
        showSections = if (context.route.params.containsKey(SHOW_SECTIONS_PARAM)) {
            context.route.params.getValue(SHOW_SECTIONS_PARAM).split("=")
                .last().toBoolean()
        } else {
            true
        }
        if (hasPostIdParam) {
            val postId = context.route.params.getValue(POST_ID_PARAM)
            apiResponse = readSelectedPosts(postId = postId)
        }
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
                content = { CategoryNavigationItems(
                    vertical = true
                ) }
            )
        }
        if (showSections) {
            HeaderSection(
                breakpoint = breakpoint,
                onMenuOpened = {
                    overflowOpened = true
                },
                logo = Res.Image.logo
            )
        }
        when (apiResponse) {
            is ApiResponse.Success -> {
                PostContent(
                    post = (apiResponse as ApiResponse.Success).data,
                    breakpoint = breakpoint
                )
                scope.launch {
                    delay(50)
                    try {
                        js("hljs.highlightAll()") as Unit
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }
            is ApiResponse.Idle -> {
                LoadingIndicator()
            }
            is ApiResponse.Error -> {
                ErrorView((apiResponse as ApiResponse.Error).message)
            }
        }
        if (showSections) {
            FooterSection()
        }
    }
}

@Composable
fun PostContent(
    post: Post,
    breakpoint: Breakpoint
) {
    LaunchedEffect(post) {
        ((document.getElementById(Id.postContent)) as HTMLDivElement).innerHTML = post.content
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 50.px, bottom = 200.px)
            .padding(leftRight = 24.px)
            .maxWidth(800.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .color(Theme.HalfBlack.rgb)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px),
            text = post.date.toLong().parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .color(Colors.Black)
                .fontFamily(FONT_FAMILY)
                .margin(bottom = 40.px)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .fontWeight(FontWeight.Bold)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                }
                .fontSize(40.px),
            text = post.title
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (breakpoint <= Breakpoint.SM) 250.px
                    else if (breakpoint <= Breakpoint.MD) 400.px
                    else 600.px)
                .margin(bottom = 40.px),
            src = post.thumbnail
        )
        Div(
            attrs = Modifier
                .fontFamily(FONT_FAMILY)
                .fillMaxWidth()
                .id(Id.postContent)
                .toAttrs()
        )
    }
}
