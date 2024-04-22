package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.components.PostsView
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.Constants.QUERY_PARAM
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.util.Constants.COLLAPSED_PANEL_HEIGHT
import com.example.blogmultiplatform.util.Constants.FONT_FAMILY
import com.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.util.Id
import com.example.blogmultiplatform.util.deleteSelectedPosts
import com.example.blogmultiplatform.util.fetchMyPosts
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.example.blogmultiplatform.util.noBorder
import com.example.blogmultiplatform.util.searchPostsByTitle
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page
@Composable
fun MyPostsPage() {
    isUserLoggedIn {
        MyPostsScreen()
    }
}
@Composable
fun MyPostsScreen() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    var selectableMode by remember { mutableStateOf(false) }
    var postsToSkip by remember { mutableStateOf(0) }
    var showMoreVisibility by remember { mutableStateOf(false) }
    var switchText by remember { mutableStateOf("Select") }
    val selectedPosts = remember { mutableStateListOf<String>() }
    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val scope = rememberCoroutineScope()

    val hasParams = remember(key1 = context.route) { context.route.params.containsKey(QUERY_PARAM) }
    val query = remember(key1 = context.route) {context.route.params[QUERY_PARAM] ?: "" }

    LaunchedEffect(key1 = context.route) {
        postsToSkip = 0
        if (hasParams) {
        (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = query.replace("%20", " ")
            searchPostsByTitle(
                query = query,
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postsToSkip += POSTS_PER_PAGE
                        showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = {
                    println(it)
                }
            )
        } else {
            fetchMyPosts(
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postsToSkip += POSTS_PER_PAGE
                        showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = {
                    println(it)
                }
            )
        }
    }
   AdminPageLayout {
       Column(
           modifier = Modifier
               .margin(topBottom = 50.px)
               .fillMaxSize()
               .padding(
                   left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px,
                   top = if (breakpoint > Breakpoint.MD) 0.px else COLLAPSED_PANEL_HEIGHT.px
               ),
           verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Box(
               modifier = Modifier
                   .margin(bottom = 24.px)
                   .fillMaxWidth(
                       if (breakpoint > Breakpoint.MD) 30.percent else 50.percent
                   ),
               contentAlignment = Alignment.Center
           ) {
               SearchBar(
                   breakpoint = breakpoint,
                   modifier = Modifier
                       .transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms))
                       .visibility(if (selectableMode) Visibility.Hidden else Visibility.Visible),
                   onEnterClick = {
                   val query =(document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                   if (query.isNotEmpty()) {
                       context.router.navigateTo(Screen.AdminMyPosts.searchByTitle(query = query))
                   } else {
                       context.router.navigateTo(Screen.AdminMyPosts.route)
                   }
               },
                   onSearchIconClick = {}
                   )
           }
           Row(
               modifier = Modifier
                   .fillMaxWidth(
                       90.percent
                   )
                   .margin(bottom = 24.px),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               Row(verticalAlignment = Alignment.Bottom) {
                   Switch(
                       modifier = Modifier.margin(right = 8.px),
                       size = SwitchSize.LG,
                       checked = selectableMode,
                       onCheckedChange = {
                           selectableMode = it
                           if (!selectableMode) {
                               switchText = "Select"
                               selectedPosts.clear()
                           } else {
                               switchText = "${selectedPosts.size} Posts selected"
                           }
                       },
                   )
                   SpanText(
                       modifier = Modifier
                           .color(if (selectableMode) Colors.Black else Theme.HalfBlack.rgb),
                       text = switchText
                   )
               }
                   Button(
                       attrs = Modifier
                           .height(54.px)
                           .padding(leftRight = 24.px)
                           .backgroundColor(Theme.Red.rgb)
                           .borderRadius(4.px)
                           .fontFamily(FONT_FAMILY)
                           .fontSize(14.px)
                           .visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden)
                           .fontWeight(FontWeight.Medium)
                           .onClick {
                               scope.launch {
                                   val result = deleteSelectedPosts(ids = selectedPosts)
                                   if (result) {
                                       selectableMode = false
                                       switchText = "Select"
                                       postsToSkip -= selectedPosts.size
                                       selectedPosts.forEach { deletedPostId ->
                                           myPosts.removeAll {
                                               it.id == deletedPostId
                                           }
                                       }
                                       selectedPosts.clear()
                                   } else {
                                       println(result)
                                   }
                               }
                           }
                           .color(Colors.White)
                           .noBorder()
                           .toAttrs()
                   ) {
                       SpanText(
                           text = "Delete"
                       )
                   }
           }
           PostsView(
               posts = myPosts,
               showMoreVisibility = showMoreVisibility,
               onSelect = {
                   selectedPosts.add(it)
                   switchText = "${selectedPosts.size} Posts selected"
               },
               onDeselect = {
                   selectedPosts.remove(it)
                   switchText = "${selectedPosts.size} Posts selected"
               },
               selectableMode = selectableMode,
               breakpoint = breakpoint,
               onClick = {
                   context.router.navigateTo(Screen.AdminCreate.passPostId(it))
               },
               onShowMoreClicked = {
                   scope.launch {
                       if (hasParams) {
                           searchPostsByTitle(
                               query = query,
                               skip = postsToSkip,
                               onSuccess = {
                                   if (it is ApiListResponse.Success) {
                                       if (it.data.isNotEmpty()) {
                                           myPosts.addAll(it.data)
                                           postsToSkip += POSTS_PER_PAGE
                                           if (it.data.size < POSTS_PER_PAGE) showMoreVisibility = false
                                       } else {
                                           showMoreVisibility = false
                                       }
                                   }
                               },
                               onError = {
                                   println(it)
                               }
                           )
                       } else {
                           fetchMyPosts(
                               skip = postsToSkip,
                               onSuccess = {
                                   if (it is ApiListResponse.Success) {
                                       if (it.data.isNotEmpty()) {
                                           myPosts.addAll(it.data)
                                           postsToSkip += POSTS_PER_PAGE
                                           if (it.data.size < POSTS_PER_PAGE) showMoreVisibility = false
                                       } else {
                                           showMoreVisibility = false
                                       }
                                   }
                               },
                               onError = {
                                   println(it)
                               }
                           )
                       }
                   }
               }
           )
       }
   }
}