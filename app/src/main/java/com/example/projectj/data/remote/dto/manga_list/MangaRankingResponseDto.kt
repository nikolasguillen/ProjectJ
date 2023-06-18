package com.example.projectj.data.remote.dto.manga_list


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaRankingResponseDto(
    @Json(name = "data")
    val data: List<RankedMangaData>,
    @Json(name = "paging")
    val paging: Paging
)