package com.example.projectj.data.remote.dto.manga_list

sealed class MangaRankingType(val rankingType: String) {
    object All : MangaRankingType("all")
    object Manga : MangaRankingType("manga")
    object Novels : MangaRankingType("novels")
    object Oneshots : MangaRankingType("oneshots")
    object Doujin : MangaRankingType("doujin")
    object Manhwa : MangaRankingType("manhwa")
    object Manhua : MangaRankingType("manhua")
    object ByPopularity : MangaRankingType("bypopularity")
    object Favorite : MangaRankingType("favorite")
}
