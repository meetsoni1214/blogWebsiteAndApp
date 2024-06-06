package com.example.blogmultiplatform.navigation

import com.example.blogmultiplatform.models.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.models.Constants.QUERY_PARAM
import com.example.blogmultiplatform.models.Constants.UPDATED_PARAM
import com.example.shared.Category



sealed class Screen(val route: String) {
    data object AdminHome: Screen(route = "/admin/")
    data object AdminLogin: Screen(route = "/admin/login")
    data object AdminCreate: Screen(route = "/admin/create") {
        fun passPostId(id: String) = "/admin/create?${POST_ID_PARAM}=$id"
    }
    data object AdminMyPosts: Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String) = "/admin/myposts?${QUERY_PARAM}=$query"
    }
    data object AdminSuccess: Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?${UPDATED_PARAM}=true"
    }
    data object HomePage: Screen(route = "/")
    data object SearchPage: Screen(route = "/search/query") {
        fun searchByCategory(category: Category) = "/search/query?${CATEGORY_PARAM}=${category.name}"
        fun searchByTitle(query: String) = "/search/query?${QUERY_PARAM}=${query}"
    }
    data object PostPage: Screen(route = "/posts/post") {
        fun getPost(id: String) = "/posts/post?${POST_ID_PARAM}=$id"
    }
}