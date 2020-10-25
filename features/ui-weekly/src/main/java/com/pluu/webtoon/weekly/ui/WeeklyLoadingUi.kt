package com.pluu.webtoon.weekly.ui

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun WeeklyLoadingUi(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 0f
) {
    CircularProgressIndicator(
        modifier = modifier.preferredWidth(60.dp).preferredHeight(60.dp),
        color = MaterialTheme.colors.secondary,
        progress = progress
    )
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun previewWeeklyLoadingUi() {
    Box(
        modifier = Modifier.fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        WeeklyLoadingUi(progress = 1f)
    }
}
