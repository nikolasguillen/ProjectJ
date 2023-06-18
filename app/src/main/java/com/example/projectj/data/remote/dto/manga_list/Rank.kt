package com.example.projectj.data.remote.dto.manga_list

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rank(
    val rank: Int
)
