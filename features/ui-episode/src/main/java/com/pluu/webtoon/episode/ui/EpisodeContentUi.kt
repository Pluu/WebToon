package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onCommit
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
    uiState: UiState<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onMoreLoaded: () -> Unit,
    onEpisodeClicked: (EpisodeInfo) -> Unit,
) {
    // TODO: 페이징 처리 개선 필요
    val rememberItems = remember { mutableStateListOf<EpisodeInfo>() }

    val circleColors = remember {
        listOf(0xFF0F9D58, 0xFFDB4437, 0xFF4285f4, 0xFFF4B400).map { it.toColor() }
    }

    onCommit(uiState) {
        when {
            uiState.initialLoad -> {
                rememberItems.clear()
            }
            uiState.data != null -> {
                rememberItems.addAll(uiState.data.orEmpty())
            }
        }
    }

    // TODO : SwipeToRefreshLayout 내부의 컨텐츠 기준으로 Swipe 처리
    LoadingContent(
        empty = uiState.initialLoad,
        emptyContent = {
            EmptyContainer(circleColors)
        },
        isLoading = uiState.loading,
        refreshColors = circleColors,
        onRefresh = onRefresh
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(rememberItems) { index, item ->
                if (rememberItems.lastIndex == index) {
                    onActive {
                        onMoreLoaded()
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

@Composable
private fun EmptyContainer(
    circleColors: List<Color>
) {
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        CircularProgressIndicator(
            colors = circleColors,
            strokeWidth = 7.dp,
            modifier = Modifier
                .preferredSize(72.dp)
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
                            .preferredSize(36.dp)
                            .padding(4.dp)
                    )
                }
            },
            content = content,
        )
    }
}
