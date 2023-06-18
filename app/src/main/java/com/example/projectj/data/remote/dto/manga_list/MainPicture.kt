package com.example.projectj.data.remote.dto.manga_list

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainPicture(
    val medium: String? = null,
    val large: String? = null
)
