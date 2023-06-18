package com.example.projectj.data.remote.dto.manga_list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Paging(
    @Json(name = "next")
    val next: String? = null
)
