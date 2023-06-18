package com.example.projectj.ui.discover_screen

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectj.R
import com.example.projectj.data.remote.MangaFields
import com.example.projectj.data.remote.MangaApi
import com.example.projectj.data.remote.dto.MangaDetails
import com.example.projectj.data.remote.dto.MangaRankingType
import com.example.projectj.data.remote.dto.RankedMangaData
import com.example.projectj.ui.utils.ProjectJContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel
@Inject
constructor(
    private val mangaApi: MangaApi
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectJMangaListUiState())
    val state = _state.asStateFlow()

    init {
        loadMangaList(isRefreshing = false)
    }

    fun loadMangaList(isRefreshing: Boolean) {
        _state.value = _state.value.copy(isLoading = true, isRefreshing = isRefreshing)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                mangaApi.getMangaRanking(
                    rankingType = MangaRankingType.Favorite.rankingType,
                    fields = listOf(
                        MangaFields.ID,
                        MangaFields.TITLE,
                        MangaFields.MAIN_PICTURE,
                        MangaFields.AUTHORS,
                        MangaFields.GENRES,
                        MangaFields.SYNOPSIS,
                        MangaFields.START_DATE,
                        MangaFields.UPDATED_AT,
                        MangaFields.PICTURES,
                        MangaFields.BACKGROUND
                    ).joinToString(separator = ",") { it }
                )
            }

                .onSuccess { response ->
                    val mangaList = response.data
                        .sortedBy { it.ranking.rank }
                        .map {
                            MangaDetails(
                                id = it.details.id,
                                title = it.details.title,
                                mainPicture = it.details.mainPicture,
                                authors = it.details.authors,
                                genres = it.details.genres,
                                synopsis = it.details.synopsis,
                                creationDate = it.details.creationDate,
                                lastUpdated = it.details.lastUpdated
                                    .substringBefore("+")
                                    .replace("T", " ")
                                ,
                                pictures = it.details.pictures,
                                background = it.details.background,
                                rank = it.ranking.rank
                            )
                        }

                    _state.value = ProjectJMangaListUiState(
                        mangaList = mangaList,
                        openedManga = mangaList.first(),
                        isLoading = false,
                        isRefreshing = false
                    )
                }

                .onFailure { throwable ->
                    throwable.printStackTrace()
                    _state.value = _state.value.copy(
                        error = "Si è verificato un errore, riprovare più tardi",
                        isLoading = false,
                        isRefreshing = false
                    )
                }
        }
    }

    fun setOpenedManga(mangaId: Int, contentType: ProjectJContentType) {
        val manga = _state.value.mangaList.find { it.id == mangaId }
        _state.value = _state.value.copy(
            openedManga = manga,
            isDetailOnlyOpen = contentType == ProjectJContentType.SINGLE_PANE
        )
    }

    fun closeDetailScreen() {
        _state.value = _state
            .value.copy(
                isDetailOnlyOpen = false,
                openedManga = _state.value.mangaList.first()
            )
    }
}

data class ProjectJMangaListUiState(
    @StringRes val pageTitleResId: Int = R.string.top_ranked_mangas,
    val mangaList: List<MangaDetails> = emptyList(),
    val openedManga: MangaDetails? = null,
    val isDetailOnlyOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)