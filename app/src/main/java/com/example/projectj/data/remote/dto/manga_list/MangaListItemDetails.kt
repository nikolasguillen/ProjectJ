package com.example.projectj.data.remote.dto.manga_list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaListItemDetails(
    val id: Int,
    val title: String,
    @Json(name = "main_picture")
    val mainPicture: MainPicture,
    val authors: List<Author>,
    val genres: List<Genre>
)
