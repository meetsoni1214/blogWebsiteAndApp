package com.example.blogmultiplatform.models

import kotlinx.serialization.Serializable

@Serializable
actual data class User(
    actual val _id: String = "",
    actual val username: String = "",
    actual val password: String = ""
)

@Serializable
actual data class UserWithoutPassword(
    actual val _id: String = "",
    actual val username: String = ""
)
