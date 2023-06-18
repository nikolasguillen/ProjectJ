package com.example.projectj.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Picture(
    val medium: String? = null,
    val large: String? = null
)
