package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pluu.compose.ui.CircularProgressIndicator
import com.pluu.compose.ui.SwipeToRefreshLayout
import com.pluu.compose.ui.graphics.toColor
import com.pluu.ui.state.UiState
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeContentUi(
    modifier: Modifier = Modifier,
    uiState: UiState<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    onRefresh: () -> Unit,
    onMoreLoaded: () -> Unit,
    onEpisodeClicked: (EpisodeInfo) -> Unit,
) {
    val rememberItems = remember { mutableStateListOf<EpisodeInfo>() }

    val circleColors = remember {
        listOf(0xFF0F9D58, 0xFFDB4437, 0xFF4285f4, 0xFFF4B400).map { it.toColor() }
    }

    DisposableEffect(uiState) {
        when {
            uiState.initialLoad -> {
                rememberItems.clear()
            }
            uiState.data != null -> {
                rememberItems.addAll(uiState.data.orEmpty())
            }
        }
        onDispose { }
    }

    when {
        uiState.initialLoad -> {
            EmptyContainer(circleColors)
        }
        uiState.loading -> {
            Surface(elevation = 10.dp, shape = CircleShape) {
                CircularProgressIndicator(
                    colors = circleColors,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                )
            }
        }
        else -> {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(rememberItems) { index, item ->
                    if (rememberItems.lastIndex == index) {
                        DisposableEffect(Unit) {
                            onMoreLoaded()
                            onDispose { }
                        }
                    }
                    EpisodeItemUi(
                        item = item,
                        isRead = readIdSet.contains(item.id),
                        onClicked = onEpisodeClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyContainer(
    circleColors: List<Color>
) {
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        CircularProgressIndicator(
            colors = circleColors,
            strokeWidth = 7.dp,
            modifier = Modifier
                .size(72.dp)
                .padding(4.dp)
        )
    }
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    isLoading: Boolean,
    refreshColors: List<Color>,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeToRefreshLayout(
            refreshingState = isLoading,
            thresholdFraction = 0.9f,
            onRefresh = onRefresh,
            refreshIndicator = {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        colors = refreshColors,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            },
            content = content,
        )
    }
}
