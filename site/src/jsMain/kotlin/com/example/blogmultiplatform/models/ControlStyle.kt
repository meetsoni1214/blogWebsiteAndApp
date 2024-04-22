package com.example.blogmultiplatform.models

sealed class ControlStyle(val style: String) {
    data class Bold(val selectedText: String?): ControlStyle(
        style = "<strong>$selectedText</strong>"
    )
    data class Italic(val selectedText: String?): ControlStyle(
        style = "<em>$selectedText</em>"
    )
    data class Link(
        val selectedText: String?,
        val href: String,
        val title: String
    ): ControlStyle(
        style = "<a href=\"$href\" title=\"$title\">$selectedText</a>"
    )

    data class Title(val selectedText: String?): ControlStyle(
        style = "<h1><strong>$selectedText</strong></h1>"
    )
    data class Subtitle(val selectedText: String?): ControlStyle(
        style = "<h2>$selectedText</h2>"
    )

    data class Quote(val selectedText: String?): ControlStyle(
        style = "<div style=\"background-color:#FAFAFA;padding:12px;border-radius:6px;\"><em>❞ $selectedText</em></div>"
    )

    data class Code(val selectedText: String?): ControlStyle(
        style = "<div style=\"padding:12px;border-radius:6px;\"><pre><code class=\"language-kotlin\"> $selectedText </code></pre></div>"
    )

    data class Image(
        val selectedText: String?,
        val imageLink: String,
        val desc: String
    ): ControlStyle(
        style = "<img src=\"$imageLink\" alt=\"$desc\" style=\"max-width: 100%\">$selectedText</img>"
    )

    data class Break(val selectedText: String?): ControlStyle(
        style = "$selectedText<br>"
    )

}
