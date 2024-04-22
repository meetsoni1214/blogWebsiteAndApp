package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.components.PostPreview
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.CSSUnit.Companion.px
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(posts: ApiListResponse, breakpoint: Breakpoint) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(PAGE_WIDTH.px)
                .backgroundColor(Theme.Secondary.rgb),
            contentAlignment = Alignment.Center
        ) {
            when (posts) {
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPosts(posts.data, breakpoint = breakpoint)
                }
                is ApiListResponse.Error -> {}
            }
        }
    }
}

@Composable
fun MainPosts(
    posts: List<PostWithoutDetails>,
    breakpoint: Breakpoint
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent else 90.percent
            )
            .margin(topBottom = 50.px),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        if (breakpoint == Breakpoint.XL) {
            PostPreview(
                postWithoutDetails = posts.first(),
                darkTheme = true,
                onClick = {},
                imageHeight = 640.px
            )
            Column(modifier = Modifier
                .fillMaxWidth(80.percent)
                .margin(left = 20.px)) {
                posts.drop(1).forEach { post ->
                    PostPreview(
                        modifier = Modifier.margin(bottom = 20.px),
                        postWithoutDetails = post,
                        imageHeight = 200.px,
                        titleMaxLines = 1,
                        vertical = false,
                        darkTheme = true,
                        onClick = {},
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.LG) {
            Box(
                modifier = Modifier.margin(right = 10.px)
            ) {
                PostPreview(
                    postWithoutDetails = posts.first(),
                    darkTheme = true,
                    onClick = {},
                )
            }
            Box(
                modifier = Modifier.margin(left = 10.px)
            ) {
                PostPreview(
                    postWithoutDetails = posts[1],
                    darkTheme = true,
                    onClick = {}
                )
            }

        } else {
            PostPreview(
                postWithoutDetails = posts.first(),
                darkTheme = true,
                onClick = {},
                imageHeight = 640.px
            )
        }
    }
}