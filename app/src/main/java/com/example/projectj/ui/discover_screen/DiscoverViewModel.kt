package com.example.projectj.ui.discover_screen

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectj.R
import com.example.projectj.data.remote.MangaFields
import com.example.projectj.data.remote.MangaListApi
import com.example.projectj.data.remote.dto.manga_list.MangaListItemDetails
import com.example.projectj.data.remote.dto.manga_list.MangaRankingType
import com.example.projectj.data.remote.dto.manga_list.RankedMangaData
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
    private val mangaListApi: MangaListApi
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
                mangaListApi.getMangaRanking(
                    rankingType = MangaRankingType.Favorite.rankingType,
                    fields = listOf(
                        MangaFields.ID,
                        MangaFields.TITLE,
                        MangaFields.MAIN_PICTURE,
                        MangaFields.AUTHORS,
                        MangaFields.GENRES
                    ).joinToString(separator = ",") { it }
                )
            }

                .onSuccess { response ->
                    val mangaList = response.data
                        .sortedBy { it.ranking.rank }
                    _state.value = ProjectJMangaListUiState(
                        mangaList = mangaList,
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
}

data class ProjectJMangaListUiState(
    @StringRes val pageTitleResId: Int = R.string.top_ranked_mangas,
    val mangaList: List<RankedMangaData> = emptyList(),
    val openedManga: MangaListItemDetails? = null,
    val isDetailOnlyOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)