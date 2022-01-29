package com.pluu.webtoon.episode.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pluu.compose.ui.CircularProgressIndicator

@Composable
fun ThemeCircularProgressIndicator(
    modifier: Modifier = Modifier,
    circleSize: Dp,
    strokeWidth: Dp = 4.0.dp,
    contentPadding: PaddingValues = PaddingValues()
) {
    val circleColors = remember {
        listOf(
            Color(0xFF0F9D58),
            Color(0xFFDB4437),
            Color(0xFF4285f4),
            Color(0xFFF4B400)
        )
    }

    CircularProgressIndicator(
        colors = circleColors,
        strokeWidth = strokeWidth,
        modifier = modifier
            .size(circleSize)
            .padding(contentPadding)
    )
}