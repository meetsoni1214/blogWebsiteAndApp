package com.example.androidapp.screens.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.MongoSync
import com.example.androidapp.model.Category
import com.example.androidapp.model.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _categoryPosts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val categoryPosts: State<RequestState<List<Post>>> = _categoryPosts

    init {
        _categoryPosts.value = RequestState.Loading
        val selectedCategory = savedStateHandle.get<String>("category")
        if (selectedCategory != null) {
            viewModelScope.launch {
                MongoSync.searchPostsByCategory(category = Category.valueOf(selectedCategory))
                    .collectLatest { _categoryPosts.value = it }
            }
        }
    }
}