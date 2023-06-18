package com.example.projectj.data.remote

import com.example.projectj.data.CLIENT_ID
import com.example.projectj.data.remote.dto.manga_list.MangaRankingResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MangaListApi {
    @GET(MANGA_RANKING_URL)
    suspend fun getMangaRanking(
        @Header("X-MAL-CLIENT-ID") clientId: String = CLIENT_ID,
        @Query("ranking_type") rankingType: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int? = null,
        @Query("fields", encoded = true) fields: String? = null
    ): MangaRankingResponseDto

    companion object {
        const val MANGA_RANKING_URL = "manga/ranking"
    }
}