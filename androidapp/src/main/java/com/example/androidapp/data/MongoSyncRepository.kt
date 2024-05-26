package com.example.androidapp.data

import com.example.androidapp.model.PostSync
import com.example.androidapp.util.RequestState
import com.example.blogmultiplatform.models.Post
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<PostSync>>>

}