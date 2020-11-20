package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pluu.compose.foundation.lazy.LazyGridFor
import com.pluu.compose.ui.SwipeToRefreshLayout
import com.pluu.webtoon.model.EpisodeInfo

// TODO: 컨텐츠 추가
// TODO: Refresh
// TODO: 읽기 처리

@Composable
fun EpisodeContentUi(
    uiState: EpisodeUiState,
    modifier: Modifier = Modifier,
    onMoreLoaded: () -> Unit,
    onEpisodeClicked: (EpisodeInfo) -> Unit,
) {
    // TODO: 페이징 처리 개선 필요
    var idx by remember { mutableStateOf(-1) }
    val rememberItems = remember { mutableStateListOf<EpisodeInfo>() }

    onCommit {
        if (uiState.idx == idx || uiState.items.isEmpty()) return@onCommit
        idx = uiState.idx

        if (idx == 0) {
            rememberItems.clear()
        }
        rememberItems.addAll(uiState.items)
    }

    LazyGridFor(
        items = rememberItems,
        rows = 2,
        modifier = modifier
    ) { item, index ->
        if (rememberItems.lastIndex == index) {
            onActive {
                onMoreLoaded()
            }
        }
        EpisodeItemUi(item = item, onClicked = onEpisodeClicked)
    }
}

@Composable
private fun EpisodeContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        // TODO: Refresh Circular
//        #0F9D58
//        #DB4437
//        #4285f4
//        #f4b400
        SwipeToRefreshLayout(
            refreshingState = isLoading,
            onRefresh = onRefresh,
            refreshIndicator = {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
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