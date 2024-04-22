package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.util.Constants
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun PostsView(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    selectableMode: Boolean = false,
    onSelect: (String) -> Unit = {},
    title: String? = null,
    onDeselect: (String) -> Unit = {},
    showMoreVisibility: Boolean,
    onShowMoreClicked: () -> Unit,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(
            90.percent
        ),
        verticalArrangement = Arrangement.Center
    ) {
        if (title != null) {
            SpanText(
                text = title,
                modifier = Modifier
                    .fontFamily(FONT_FAMILY)
                    .fontWeight(FontWeight.Medium)
                    .margin(bottom = 24.px)
                    .fontSize(18.px)
            )
        }
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2, md = 3, lg = 4)
        ) {
            posts.forEach {
                PostPreview(
                    selectableMode = selectableMode,
                    onSelect = onSelect,
                    onDeselect = onDeselect,
                    postWithoutDetails = it,
                    onClick = onClick
                )
            }
        }
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .margin(topBottom = 50.px)
                .textAlign(TextAlign.Center)
                .fontFamily(Constants.FONT_FAMILY)
                .fontWeight(FontWeight.Medium)
                .fontSize(16.px)
                .cursor(Cursor.Pointer)
                .visibility(if (showMoreVisibility) Visibility.Visible else Visibility.Hidden)
                .onClick { onShowMoreClicked() },
            text = "Show More"
        )
    }
}