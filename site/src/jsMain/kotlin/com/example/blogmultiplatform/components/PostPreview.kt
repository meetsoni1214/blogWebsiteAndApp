package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.PostPreviewStyle
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.parseDateString
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    postWithoutDetails: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    titleMaxLines: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    imageHeight: CSSSizeValue<CSSUnit.px> = 250.px,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick: (String) -> Unit,
) {
    var checked by remember(selectableMode) { mutableStateOf(false) }
    val context = rememberPageContext()
    if (vertical) {
        Column(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
                .fillMaxWidth(if (darkTheme) 100.percent
                else if (titleColor == Theme.Sponsored.rgb) 100.percent
                else 95.percent)
                .margin(bottom = 24.px)
                .borderRadius(4.px)
                .border(
                    width = if (checked) 4.px else 0.px,
                    style = if (checked) LineStyle.Solid else LineStyle.None,
                    color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
                )
                .transition(CSSTransition(TransitionProperty.All, 200.ms))
                .onClick {
                    if (selectableMode) {
                        checked = !checked
                        if (checked) {
                            onSelect(postWithoutDetails.id)
                        } else {
                            onDeselect(postWithoutDetails.id)
                        }
                    } else {
                        onClick(postWithoutDetails.id)
                    }
                }
                .cursor(Cursor.Pointer)
                .backgroundColor(if (!darkTheme) Theme.LightGray.rgb else Colors.Transparent)
        ) {
            PostContent(
                postWithoutDetails = postWithoutDetails,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                imageHeight = imageHeight,
                titleMaxLines = titleMaxLines,
                vertical = vertical,
                titleColor = titleColor
            )
        }
    } else {
        Row(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
                .onClick { onClick(postWithoutDetails.id) }
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                postWithoutDetails = postWithoutDetails,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                imageHeight = imageHeight,
                titleMaxLines = titleMaxLines,
                checked = checked,
                vertical = vertical,
                titleColor = titleColor
            )
        }
    }
}

@Composable
fun PostContent(
    postWithoutDetails: PostWithoutDetails,
    darkTheme: Boolean,
    selectableMode: Boolean,
    imageHeight: CSSSizeValue<CSSUnit.px>,
    titleColor: CSSColorValue,
    titleMaxLines: Int,
    vertical: Boolean,
    checked: Boolean
) {
    Image(
        src = postWithoutDetails.thumbnail,
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .width(if (vertical) 100.percent else 300.px)
            .height(imageHeight)
            .objectFit(ObjectFit.Cover),
        description = "Post Thumbnail Image"
    )
    Column(
        modifier = Modifier
            .thenIf(
                condition = !vertical,
                other = Modifier.margin(left = 20.px)
            )
            .padding(all = 12.px)
            .fillMaxWidth()
    ) {
        SpanText(
            modifier = Modifier
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb)
                .fontSize(12.px)
                .fontFamily(FONT_FAMILY),
            text = postWithoutDetails.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .color(if (darkTheme) Colors.White else titleColor)
                .margin(bottom = 12.px)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .fontFamily(FONT_FAMILY)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaxLines")
                    property("line-clamp", "$titleMaxLines")
                    property("-webkit-box-orient", "vertical")
                },
            text = postWithoutDetails.title
        )
        SpanText(
            modifier = Modifier
                .color(if (darkTheme) Colors.White else Colors.Black)
                .fontSize(16.px)
                .margin(bottom = 8.px)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .fontFamily(FONT_FAMILY)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                },
            text = postWithoutDetails.subtitle
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryChip(
                category = postWithoutDetails.category,
                darkTheme = darkTheme
            )
            if (selectableMode) {
                CheckboxInput(
                    checked = checked,
                    attrs = Modifier
                        .size(20.px)
                        .toAttrs()
                )
            }
        }
    }
}

