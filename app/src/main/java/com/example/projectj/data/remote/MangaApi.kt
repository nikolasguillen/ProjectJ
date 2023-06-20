package com.example.projectj.data.remote

import com.example.projectj.data.CLIENT_ID
import com.example.projectj.data.remote.dto.MangaDetails
import com.example.projectj.data.remote.dto.MangaRankingResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaApi {
    @GET(MANGA_RANKING_URL)
    suspend fun getMangaRanking(
        @Header("X-MAL-CLIENT-ID") clientId: String = CLIENT_ID,
        @Query("ranking_type") rankingType: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int? = null,
        @Query("fields", encoded = true) fields: String? = null
    ): MangaRankingResponseDto

    @GET(MANGA_DETAILS_URL)
    suspend fun getMangaDetails(
        @Header("X-MAL-CLIENT-ID") clientId: String = CLIENT_ID,
        @Path("mangaId") mangaId: Int,
        @Query("fields", encoded = true) fields: String? = null
    ): MangaDetails

    companion object {
        const val MANGA_RANKING_URL = "manga/ranking"
        const val MANGA_DETAILS_URL = "manga/{mangaId}"
    }
}