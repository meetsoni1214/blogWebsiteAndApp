package com.example.blogmultiplatform.models

import com.example.blogmultiplatform.models.PostWithoutDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(ApiListResponseSerializer::class)
 sealed class ApiListResponse {
    @Serializable
    @SerialName("idle")
     object Idle: ApiListResponse()
    @Serializable
    @SerialName("success")
     data class Success(val data: List<PostWithoutDetails>): ApiListResponse()
    @Serializable
    @SerialName("error")
     data class Error(val message: String): ApiListResponse()
}

object ApiListResponseSerializer: JsonContentPolymorphicSerializer<ApiListResponse>(ApiListResponse::class) {
    override fun selectDeserializer(element: JsonElement) = when  {
        "data" in element.jsonObject -> ApiListResponse.Success.serializer()
        "message" in element.jsonObject -> ApiListResponse.Error.serializer()
        else -> ApiListResponse.Idle.serializer()
    }

}
