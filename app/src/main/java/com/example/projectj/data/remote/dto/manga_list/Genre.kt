package com.example.projectj.data.remote.dto.manga_list


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)