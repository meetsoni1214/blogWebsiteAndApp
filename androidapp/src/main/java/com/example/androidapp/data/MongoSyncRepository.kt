package com.example.androidapp.data

import com.example.androidapp.model.Category
import com.example.androidapp.model.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<Post>>>
    fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>>

    fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>>

}