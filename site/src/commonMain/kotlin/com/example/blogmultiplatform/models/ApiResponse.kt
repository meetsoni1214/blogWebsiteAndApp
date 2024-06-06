package com.example.blogmultiplatform.models

import com.example.blogmultiplatform.models.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(ApiResponseSerializer::class)
 sealed class ApiResponse {
    @Serializable
    @SerialName("idle")
    object Idle: ApiResponse()
    @Serializable
    @SerialName("success")
    data class Success(val data: Post): ApiResponse()
    @Serializable
    @SerialName("error")
    data class Error(val message: String): ApiResponse()
}

object ApiResponseSerializer: JsonContentPolymorphicSerializer<ApiResponse>(ApiResponse::class) {
    override fun selectDeserializer(element: JsonElement) = when  {
        "data" in element.jsonObject -> ApiResponse.Success.serializer()
        "message" in element.jsonObject -> ApiResponse.Error.serializer()
        else -> ApiResponse.Idle.serializer()
    }
}