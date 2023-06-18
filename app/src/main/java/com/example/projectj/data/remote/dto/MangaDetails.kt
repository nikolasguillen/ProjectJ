package com.example.projectj.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDetails(
    val id: Int,
    val title: String,
    @Json(name = "main_picture")
    val mainPicture: MainPicture,
    val authors: List<Author>,
    val genres: List<Genre>,
    val synopsis: String,
    @Json(name = "start_date")
    val creationDate: String,
    @Json(name = "updated_at")
    val lastUpdated: String,
    @Json(name = "pictures")
    val pictures: List<Picture>?,
    @Json(name = "background")
    val background: String?,
    @Json(name = "rank")
    val rank: Int?
)