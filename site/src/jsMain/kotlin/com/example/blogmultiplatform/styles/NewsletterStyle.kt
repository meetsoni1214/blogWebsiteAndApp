package com.example.blogmultiplatform.styles


import com.example.shared.JsTheme
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.focus
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

val NewsletterInputStyle by ComponentStyle {
    base {
        Modifier
            .outline(
                width = 1.px,
                style = LineStyle.Solid,
                color = Color.transparent
            )
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Color.transparent
            )
            .transition(CSSTransition(property = TransitionProperty.All, duration = 300.ms))
    }
    focus {
        Modifier
            .outline(
                width = 1.px,
                style = LineStyle.Solid,
                color = JsTheme.Primary.rgb,
            )
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = JsTheme.Primary.rgb
            )
    }
}