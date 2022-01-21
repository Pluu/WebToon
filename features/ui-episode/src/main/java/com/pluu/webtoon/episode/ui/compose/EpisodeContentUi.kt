package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pluu.compose.runtime.rememberMutableStateListOf
import com.pluu.compose.ui.CircularProgressIndicator
import com.pluu.ui.state.UiState
import com.pluu.webtoon.model.EpisodeInfo

@Composable
fun EpisodeContentUi(
    modifier: Modifier = Modifier,
    uiState: UiState<List<EpisodeInfo>>,
    content: @Composable (List<EpisodeInfo>) -> Unit
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

    Box(modifier = modifier) {
        when {
            uiState.initialLoad -> {
                EmptyContainer(circleColors = circleColors)
            }
            uiState.loading -> {
                EpisodeLoadingContent(circleColors = circleColors)
            }
            else -> {
                content(rememberItems)
            }
        }
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
            modifier = Modifier
                .size(36.dp)
                .padding(4.dp)
        )
    }
}
