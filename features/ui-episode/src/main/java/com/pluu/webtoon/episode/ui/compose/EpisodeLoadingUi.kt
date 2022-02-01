package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.episode.compose.ThemeCircularProgressIndicator

@Composable
internal fun EpisodeLoadingUi(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        ThemeCircularProgressIndicator(
            circleSize = 72.dp,
            strokeWidth = 6.dp
        )
    }
}
