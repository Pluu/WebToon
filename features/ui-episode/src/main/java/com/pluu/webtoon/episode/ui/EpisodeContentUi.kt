package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pluu.compose.foundation.InfiniteListHandler
import com.pluu.compose.runtime.rememberMutableStateListOf
import com.pluu.compose.ui.CircularProgressIndicator
import com.pluu.ui.state.UiState
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo

@Composable
fun EpisodeContentUi(
    modifier: Modifier = Modifier,
    uiState: UiState<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    onMoreLoaded: () -> Unit,
    onEpisodeClicked: (EpisodeInfo) -> Unit,
) {
    val rememberItems = rememberMutableStateListOf<EpisodeInfo>()

    val circleColors = remember {
        listOf(
            Color(0xFF0F9D58),
            Color(0xFFDB4437),
            Color(0xFF4285f4),
            Color(0xFFF4B400)
        )
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
            EmptyContainer(modifier, circleColors)
        }
        uiState.loading -> {
            EpisodeLoadingContent(modifier, circleColors)
        }
        else -> {
            EpisodeGridContent(modifier, rememberItems, onMoreLoaded, readIdSet, onEpisodeClicked)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EpisodeGridContent(
    modifier: Modifier,
    items: List<EpisodeInfo>,
    onMoreLoaded: () -> Unit,
    readIdSet: Set<EpisodeId>,
    onEpisodeClicked: (EpisodeInfo) -> Unit
) {
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        cells = GridCells.Fixed(2),
        state = listState
    ) {
        items(items) { item ->
            EpisodeItemUi(
                item = item,
                isRead = readIdSet.contains(item.id),
                onClicked = onEpisodeClicked
            )
        }
    }

    InfiniteListHandler(
        listState = listState,
        buffer = 3
    ) {
        onMoreLoaded()
    }
}

@Composable
private fun EmptyContainer(
    modifier: Modifier = Modifier,
    circleColors: List<Color>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
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
private fun EpisodeLoadingContent(
    modifier: Modifier,
    circleColors: List<Color>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator(
            colors = circleColors,
            modifier = Modifier
                .size(36.dp)
                .padding(4.dp)
        )
    }
}
