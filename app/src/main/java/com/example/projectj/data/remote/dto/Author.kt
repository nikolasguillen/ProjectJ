package com.example.projectj.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "node")
    val authorDetails: AuthorDetails,
    @Json(name = "role")
    val role: String
) {
    @JsonClass(generateAdapter = true)
    data class AuthorDetails(
        @Json(name = "id")
        val id: Int,
        @Json(name = "first_name")
        val firstName: String?,
        @Json(name = "last_name")
        val lastName: String?
    )
}