package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.components.ControlPopup
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.models.ApiResponse
import com.example.shared.Category
import com.example.shared.JsTheme
import com.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.models.ControlStyle
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.models.Post

import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.example.blogmultiplatform.util.Constants.COLLAPSED_PANEL_HEIGHT
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.addPost
import com.example.blogmultiplatform.util.applyControlStyle
import com.example.blogmultiplatform.util.applyStyle
import com.example.blogmultiplatform.util.getEditor
import com.example.blogmultiplatform.util.getSelectedText
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.example.blogmultiplatform.util.noBorder
import com.example.blogmultiplatform.util.readSelectedPosts
import com.example.blogmultiplatform.util.updatePost
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.browser.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = true,
    var content: String = "",
    var category: Category = Category.Programming,
    var buttonText: String = "Create",
    var popular: Boolean = false,
    var main: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var messagePopup: Boolean = false,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false
) {
    fun reset() = this.copy(
        id = "",
        title = "",
        subtitle = "",
        thumbnail = "",
        content = "",
        category = Category.Programming,
        buttonText = "Create",
        main = false,
        popular = false,
        sponsored = false,
        editorVisibility = true,
        messagePopup = false,
        linkPopup = false,
        imagePopup = false,
    )
}
@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}
@Composable
fun CreateScreen() {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    var uiState by remember { mutableStateOf(CreatePageUiState()) }

    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(hasPostIdParam) {
        if (hasPostIdParam) {
            val postId = context.route.params[POST_ID_PARAM] ?: ""
            val response = readSelectedPosts(postId = postId)
            if (response is ApiResponse.Success) {
                (document.getElementById(Id.editor) as HTMLTextAreaElement).value =
                    response.data.content
                uiState = uiState.copy(
                    id = response.data._id,
                    title = response.data.title,
                    subtitle = response.data.subtitle,
                    thumbnail = response.data.thumbnail,
                    buttonText = "Update",
                    category = response.data.category,
                    content = response.data.content,
                    sponsored = response.data.sponsored,
                    main = response.data.main,
                    popular = response.data.popular,
                )
            }
        } else {
//            (document.getElementById(Id.editor) as HTMLTextAreaElement).value = ""
            uiState = uiState.reset()
        }
    }
   AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (breakpoint > Breakpoint.MD) 0.px else COLLAPSED_PANEL_HEIGHT.px,
                    left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px)
                .margin(topBottom = 50.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleGrid(numColumns = numColumns(base = 1, sm = 3)) {
                    Row(
                        modifier = Modifier
                            .margin(
                                right = 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(right = 8.px),
                            checked = uiState.popular,
                            onCheckedChange = {uiState = uiState.copy(popular = it)},
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Popular",
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(JsTheme.HalfBlack.rgb)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .margin(
                                right = 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(right = 8.px),
                            checked = uiState.main,
                            onCheckedChange = {uiState = uiState.copy(main = it)},
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Main",
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(JsTheme.HalfBlack.rgb)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .margin(bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(right = 8.px),
                            checked = uiState.sponsored,
                            onCheckedChange = {uiState = uiState.copy(sponsored = it)},
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Sponsored",
                            modifier = Modifier
                                .fontFamily(FONT_FAMILY)
                                .fontSize(14.px)
                                .color(JsTheme.HalfBlack.rgb)
                        )
                    }
                }
                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .fillMaxWidth()
                        .height(54.px)
                        .id(Id.titleInput)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(JsTheme.LightGray.rgb)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs {
                            attr("placeholder", "Title")
                            attr("value", uiState.title)
                        }
                )
                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .fillMaxWidth()
                        .height(54.px)
                        .id(Id.subtitleInput)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(JsTheme.LightGray.rgb)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs {
                            attr("placeholder", "Subtitle")
                            attr("value", uiState.subtitle)
                        }
                )
                CategoryDropdown(
                    selectedCategory = uiState.category,
                    onCategorySelected = {uiState = uiState.copy(category = it)}
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(topBottom = 12.px),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Switch(
                        modifier = Modifier
                            .margin(right = 8.px),
                        checked = !uiState.thumbnailInputDisabled,
                        onCheckedChange = {uiState = uiState.copy(thumbnailInputDisabled = !it)},
                        size = SwitchSize.MD
                    )
                    SpanText(
                        text = "Paste an Image URL instead",
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .color(JsTheme.HalfBlack.rgb)
                    )
                }
                ThumbnailUploader(
                    thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = uiState.thumbnailInputDisabled,
                    onThumbnailSelect = { filename, file ->
                        (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value = filename
                        uiState = uiState.copy(thumbnail = file)
                    }
                )
                EditorControls(
                    editorVisibility = uiState.editorVisibility,
                    breakpoint = breakpoint,
                    onPreviewClicked = { uiState = uiState.copy(editorVisibility = it)},
                    onLinkClick = { uiState = uiState.copy(linkPopup = true)},
                    onImageClick = { uiState = uiState.copy(imagePopup = true)}
                )
                Editor(editorVisibility = uiState.editorVisibility, content = uiState.content)
                CreateButton(onClick = {
                    uiState = uiState.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                    uiState = uiState.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                    uiState = uiState.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)
                    if (!uiState.thumbnailInputDisabled) {
                        uiState = uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                    }
                    if (
                        uiState.title.isNotEmpty() &&
                        uiState.subtitle.isNotEmpty() &&
                        uiState.thumbnail.isNotEmpty() &&
                        uiState.content.isNotEmpty()
                    ) {

                        scope.launch {
                            if (hasPostIdParam) {
                                val result = updatePost(
                                    Post(
                                        _id = uiState.id,
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        content = uiState.content,
                                        thumbnail = uiState.thumbnail,
                                        category = uiState.category,
                                        main = uiState.main,
                                        popular = uiState.popular,
                                        sponsored = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.postUpdated())
                                }
                            } else {
                                val result = addPost(
                                    Post(
                                        author = localStorage.getItem("username").toString(),
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        content = uiState.content,
                                        date = Date.now(),
                                        thumbnail = uiState.thumbnail,
                                        category = uiState.category,
                                        main = uiState.main,
                                        popular = uiState.popular,
                                        sponsored = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.route)
                                }
                            }
                    }
                } else {
                    scope.launch {
                        uiState = uiState.copy(messagePopup = true)
                        delay(2000)
                        uiState = uiState.copy(messagePopup = false)
                    }
                }
                },
                    buttonText = uiState.buttonText
                )
            }
        }
   }
    if (uiState.messagePopup) {
        MessagePopup(
        message = "Please fill out all fields!",
        onDialogDismiss = { uiState = uiState.copy(messagePopup = false)}
        )
    }
    if (uiState.linkPopup) {
        ControlPopup(
            editorControl = EditorControl.Link,
            onDialogDismiss = { uiState = uiState.copy(linkPopup = false) },
            onAddClick = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(),
                        href = href,
                        title = title
                    )
                )
            }
        )
    }
    if (uiState.imagePopup) {
        ControlPopup(
            editorControl = EditorControl.Image,
            onDialogDismiss = { uiState = uiState.copy(imagePopup = false) },
            onAddClick = { imageURI, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageLink = imageURI,
                        desc = description
                    )
                )
            }
        )
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .classNames("dropdown")
            .margin(bottom = 12.px)
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(JsTheme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle", "dropdown")
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(leftRight = 20.px)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(16.px)
                    .fontFamily(FONT_FAMILY),
                text = selectedCategory.name
            )
            Box(modifier = Modifier.classNames("dropdown-toggle"))
        }
        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.entries.forEach { category ->
                Li {
                    A(
                        attrs = Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(16.px)
                            .onClick { onCategorySelected(category) }
                            .toAttrs()
                    ) {
                        Text(value = category.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px)
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .fillMaxSize()
                .id(Id.thumbnailInput)
                .margin(right = 12.px)
                .borderRadius(4.px)
                .padding(leftRight = 20.px)
                .backgroundColor(JsTheme.LightGray.rgb)
                .noBorder()
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs{
                    attr("placeholder", "Thumbnail")
                    attr("value", thumbnail)
                }
        )
        Button(
            attrs = Modifier
                .onClick {
                    document.loadDataUrlFromDisk(
                        accept = "image/png, image/jpeg",
                        onLoad = {
                            onThumbnailSelect(filename, it)
                        }
                    )
                }
                .backgroundColor(if (!thumbnailInputDisabled) JsTheme.Gray.rgb else JsTheme.Primary.rgb)
                .color(if (!thumbnailInputDisabled) JsTheme.DarkGray.rgb else JsTheme.White.rgb)
                .padding(leftRight = 20.px)
                .fillMaxHeight()
                .borderRadius(4.px)
                .noBorder()
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .fontWeight(FontWeight.Medium)
                .thenIf(
                    condition = !thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs()
        ) {
            SpanText(
                text = "Upload"
            )
        }
    }
}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
    onPreviewClicked: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)) {
            Row(modifier = Modifier
                .backgroundColor(JsTheme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .height(54.px)) {
                EditorControl.entries.forEach {
                    EditorControlView(
                        control = it,
                        onClick = {
                            applyControlStyle(
                               editorControl =  it,
                                onLinkClick = onLinkClick,
                                onImageClick = onImageClick
                            )
                        }
                    )
                }
            }
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier.fillMaxWidth()
                        )
                        .padding(leftRight = 24.px)
                        .borderRadius(4.px)
                        .backgroundColor(
                            if (editorVisibility) {
                                JsTheme.LightGray.rgb
                            } else {
                                JsTheme.Primary.rgb
                            }
                        )
                        .color(
                            if (editorVisibility)JsTheme.DarkGray.rgb
                            else JsTheme.White.rgb
                        )
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .noBorder()
                        .onClick {
                            onPreviewClicked(!editorVisibility)
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .fontSize(14.px),
                        text = "Preview")
                }
            }

        }
    }
}

@Composable
fun EditorControlView(
    control: EditorControl,
    onClick: () -> Unit
) {
    Box(
        modifier = EditorKeyStyle.toModifier()
            .fillMaxHeight()
            .onClick { onClick() }
            .padding(leftRight = 12.px)
            .cursor(Cursor.Pointer)
            .borderRadius(r = 4.px),
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = control.icon,
            description = "${control.name} Icon"
        )
    }
}

@Composable
fun Editor(editorVisibility: Boolean, content: String) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextArea(
            attrs = Modifier
                .id(Id.editor)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(JsTheme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey) {
                        applyStyle(
                            controlStyle = ControlStyle.Break(
                                selectedText = getSelectedText()
                            )
                        )
                    }
                }
                .visibility(
                    if (editorVisibility) Visibility.Visible
                    else Visibility.Hidden
                )
                .toAttrs{
                    attr("placeholder", "Type here...")
                    attr("value", content)
                }
        )
        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(JsTheme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(
                    if (editorVisibility) Visibility.Hidden
                    else Visibility.Visible
                )
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {

        }
    }
}

@Composable
fun CreateButton(onClick: () -> Unit, buttonText: String) {
    Button(
        attrs = Modifier
            .onClick { onClick() }
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(JsTheme.Primary.rgb)
            .color(Colors.White)
            .fontFamily(FONT_FAMILY)
            .borderRadius(r = 4.px)
            .noBorder()
            .toAttrs()
    ) {
        SpanText(text = buttonText)
    }
}