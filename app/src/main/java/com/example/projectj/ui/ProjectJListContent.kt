package com.example.projectj.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import coil.compose.AsyncImage
import com.example.projectj.data.remote.dto.MangaDetails
import com.example.projectj.ui.components.ProjectJMangaListItem
import com.example.projectj.ui.discover_screen.ProjectJMangaListUiState
import com.example.projectj.ui.utils.ProjectJContentType
import com.example.projectj.ui.utils.ProjectJNavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

@Composable
fun ProjectJMangaListScreen(
    state: ProjectJMangaListUiState,
    onRefresh: () -> Unit,
    contentType: ProjectJContentType,
    navigationType: ProjectJNavigationType,
    displayFeatures: List<DisplayFeature>,
    navigateToDetail: (Int, ProjectJContentType) -> Unit,
    closeDetailScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            Text(text = state.error, style = MaterialTheme.typography.displaySmall)
        }
    }

    if (contentType == ProjectJContentType.DUAL_PANE) {
        TwoPane(
            first = {
                ProjectJMangaList(
                    pageTitle = stringResource(id = state.pageTitleResId),
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    isLoading = state.isLoading,
                    mangas = state.mangaList,
                    openedManga = state.openedManga,
                    mangaLazyListState = mangaLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                ProjectJMangaDetail(
                    manga = state.openedManga ?: state.mangaList.firstOrNull(),
                    isLoading = state.isLoading,
                    isFullscreen = false
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            ProjectJSinglePaneContent(
                state = state,
                onRefresh = onRefresh,
                mangaLazyListState = mangaLazyListState,
                navigateToDetail = navigateToDetail,
                closeDetailScreen = closeDetailScreen
            )
        }
    }
}

@Composable
fun ProjectJSinglePaneContent(
    state: ProjectJMangaListUiState,
    onRefresh: () -> Unit,
    mangaLazyListState: LazyListState,
    navigateToDetail: (Int, ProjectJContentType) -> Unit,
    closeDetailScreen: () -> Unit
) {

    Crossfade(
        targetState = state.openedManga != null && state.isDetailOnlyOpen,
        label = "SinglePaneContentCrossfade"
    ) {
        if (it) {
            BackHandler {
                closeDetailScreen()
            }
            ProjectJMangaDetail(
                manga = state.openedManga,
                isLoading = state.isLoading,
                isFullscreen = true
            ) {
                closeDetailScreen()
            }
        } else {
            ProjectJMangaList(
                pageTitle = stringResource(id = state.pageTitleResId),
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                isLoading = state.isLoading,
                mangas = state.mangaList,
                openedManga = state.openedManga,
                mangaLazyListState = mangaLazyListState,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectJMangaList(
    pageTitle: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    mangas: List<MangaDetails>,
    openedManga: MangaDetails?,
    mangaLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int, ProjectJContentType) -> Unit
) {

    val firstItemVisible by remember {
        derivedStateOf {
            mangaLazyListState.firstVisibleItemIndex == 0
        }
    }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Column(modifier = modifier.padding(16.dp)) {
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
                    .padding(start = 16.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .height(80.dp)
            )
        }

        Box(modifier = modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                state = mangaLazyListState,
                userScrollEnabled = !isLoading,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isLoading) {
                    items(items = mangas, key = { it.id }) { manga ->
                        ProjectJMangaListItem(
                            manga = manga,
                            navigateToDetail = {
                                navigateToDetail(
                                    manga.id,
                                    ProjectJContentType.SINGLE_PANE
                                )
                            },
                            isOpened = openedManga?.id == manga.id,
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
fun ProjectJMangaDetail(
    manga: MangaDetails?,
    isLoading: Boolean,
    isFullscreen: Boolean,
    onBackPressed: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = manga) {
        scrollState.scrollTo(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(all = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (manga != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                AsyncImage(
                    model = manga.mainPicture.large,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(.45f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = manga.title,
                        style = MaterialTheme.typography.displaySmall,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row {
                        Text(
                            text = "Authors: ",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(text = manga.authors.joinToString { "${it.authorDetails.firstName} ${it.authorDetails.lastName}" })
                    }
                    Row {
                        Text(
                            text = "Year: ",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = manga.creationDate.trim().split("-").first(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Row {
                        Text(
                            text = "Genre: ",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(text = manga.genres.joinToString(limit = 5, truncated = "") { it.name }
                            .removeSuffix(", "), style = MaterialTheme.typography.bodyLarge)
                    }
                    Row {
                        Text(
                            text = "Last update: ",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = manga.lastUpdated,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Text(
                text = "Synopsis:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Text(text = manga.synopsis, style = MaterialTheme.typography.bodyLarge)
        } else {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
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