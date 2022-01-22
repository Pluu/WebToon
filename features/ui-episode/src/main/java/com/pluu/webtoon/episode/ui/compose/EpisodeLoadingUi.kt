package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.pluu.webtoon.episode.compose.ThemeCircularProgressIndicator
import com.pluu.webtoon.model.EpisodeInfo

@Composable
internal fun EpisodeLoadingUi(
    modifier: Modifier = Modifier,
    episodeList: LazyPagingItems<EpisodeInfo>,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (episodeList.loadState.refresh == LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                ThemeCircularProgressIndicator(
                    circleSize = 72.dp,
                    strokeWidth = 6.dp
                )
            }
        }

        content()
    }
}
