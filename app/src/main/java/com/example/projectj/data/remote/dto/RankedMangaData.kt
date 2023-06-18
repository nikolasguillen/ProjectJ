package com.example.projectj.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankedMangaData(
    @Json(name = "node")
    val details: MangaDetails,
    val ranking: Rank
)