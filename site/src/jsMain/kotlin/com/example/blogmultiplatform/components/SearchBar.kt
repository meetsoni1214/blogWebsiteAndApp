package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.shared.JsTheme
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.noBorder
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Input

@Composable
fun SearchBar(
    breakpoint: Breakpoint,
    onEnterClick: () -> Unit,
    onSearchIconClick: (Boolean) -> Unit,
    fullWidth: Boolean = true,
    darkTheme: Boolean = false,
    modifier: Modifier = Modifier
) {
    var focused by remember { mutableStateOf(false) }
    LaunchedEffect(breakpoint) {
        if (breakpoint >= Breakpoint.SM) {
            onSearchIconClick(false)
        }
    }
    if (breakpoint >= Breakpoint.SM || fullWidth) {
        Row(
            modifier = modifier
                .thenIf(
                    condition = fullWidth,
                    other = Modifier.fillMaxWidth()
                )
                .height(54.px)
                .borderRadius(100.px)
                .backgroundColor(if (darkTheme) JsTheme.Tertiary.rgb else JsTheme.LightGray.rgb)
                .padding(left = 20.px)
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = if (focused && !darkTheme) JsTheme.Primary.rgb
                    else if (focused && darkTheme) JsTheme.Primary.rgb
                    else if (!focused && !darkTheme) JsTheme.LightGray.rgb
                    else if (!focused && darkTheme) JsTheme.Secondary.rgb
                    else JsTheme.LightGray.rgb
                )
                .transition(CSSTransition(TransitionProperty.All, duration = 200.ms)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FaMagnifyingGlass(
                modifier = Modifier
                    .color(if (focused) JsTheme.Primary.rgb else JsTheme.DarkGray.rgb)
                    .margin(right = 14.px),
                size = IconSize.SM
            )
            Input(
                type = InputType.Text,
                attrs = Modifier
                    .fillMaxSize()
                    .id(Id.adminSearchBar)
                    .color(if (darkTheme) Colors.White else Colors.Black)
                    .backgroundColor(Colors.Transparent)
                    .noBorder()
                    .onFocusIn { focused = true }
                    .onFocusOut { focused = false }
                    .onKeyDown {
                        if (it.key == "Enter") {
                            onEnterClick()
                        }
                    }
                    .toAttrs {
                        attr("placeholder", "Search...")
                    }
            )
        }
    } else {
        FaMagnifyingGlass(
            modifier = Modifier
                .color(JsTheme.Primary.rgb)
                .onClick { onSearchIconClick(true) }
                .cursor(Cursor.Pointer)
                .margin(right = 14.px),
            size = IconSize.SM
        )
    }
}