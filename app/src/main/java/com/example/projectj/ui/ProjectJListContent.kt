package com.example.projectj.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.layout.DisplayFeature
import com.example.projectj.data.remote.dto.manga_list.MangaListItemDetails
import com.example.projectj.data.remote.dto.manga_list.RankedMangaData
import com.example.projectj.ui.components.ProjectJMangaListItem
import com.example.projectj.ui.discover_screen.DiscoverViewModel
import com.example.projectj.ui.discover_screen.ProjectJMangaListUiState
import com.example.projectj.ui.utils.ProjectJContentType
import com.example.projectj.ui.utils.ProjectJNavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun ProjectJMangaListScreen(
    viewModel: DiscoverViewModel,
    contentType: ProjectJContentType,
    navigationType: ProjectJNavigationType,
    displayFeatures: List<DisplayFeature>,
    navigateToDetail: (Long, ProjectJContentType) -> Unit,
    modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = contentType) {
        if (contentType == ProjectJContentType.SINGLE_PANE && !state.isDetailOnlyOpen) {
//            TODO closeDetailScreen()
        }
    }

    val mangaLazyListState = rememberLazyListState()

    if (state.error != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = state.error!!, style = MaterialTheme.typography.displaySmall)
        }
    }

    if (contentType == ProjectJContentType.DUAL_PANE) {
        TwoPane(
            first = {
                ProjectJMangaList(
                    pageTitle = stringResource(id = state.pageTitleResId),
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.loadMangaList(isRefreshing = true) },
                    loading = state.isLoading,
                    mangas = state.mangaList,
                    openedManga = state.openedManga,
                    mangaLazyListState = mangaLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                ProjectJMangaDetail()
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            ProjectJSinglePaneContent(
                state = state,
                onRefresh = { viewModel.loadMangaList(isRefreshing = true) },
                mangaLazyListState = mangaLazyListState,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
fun ProjectJSinglePaneContent(
    state: ProjectJMangaListUiState,
    onRefresh: () -> Unit,
    mangaLazyListState: LazyListState,
    navigateToDetail: (Long, ProjectJContentType) -> Unit
) {

    ProjectJMangaList(
        pageTitle = stringResource(id = state.pageTitleResId),
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        loading = state.isLoading,
        mangas = state.mangaList,
        openedManga = state.openedManga,
        mangaLazyListState = mangaLazyListState,
        navigateToDetail = navigateToDetail
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProjectJMangaList(
    pageTitle: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    loading: Boolean,
    mangas: List<RankedMangaData>,
    openedManga: MangaListItemDetails?,
    mangaLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, ProjectJContentType) -> Unit
) {

    val firstItemVisible by remember {
        derivedStateOf {
            mangaLazyListState.firstVisibleItemIndex == 0
        }
    }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = firstItemVisible || mangaLazyListState.isScrollingUp(),
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = pageTitle,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
        }

        Box(modifier = modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                state = mangaLazyListState,
                userScrollEnabled = !loading,
                contentPadding = PaddingValues(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!loading) {
                    items(items = mangas, key = { it.details.id }) { manga ->
                        ProjectJMangaListItem(
                            manga = manga,
                            navigateToDetail = {
                                navigateToDetail(
                                    manga.details.id.toLong(),
                                    ProjectJContentType.SINGLE_PANE
                                )
                            },
                            isOpened = openedManga?.id == manga.details.id,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    items(10) {
                        Box(
                            modifier = Modifier
                                .height(170.dp)
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
                                .placeholder(
                                    visible = true,
                                    color = Color.LightGray,
                                    shape = CardDefaults.shape,
                                    highlight = PlaceholderHighlight.shimmer()
                                )
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ProjectJMangaDetail() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(10) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .placeholder(visible = true, color = Color.LightGray)
            )
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}