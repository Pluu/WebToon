package com.pluu.webtoon.episode.compose

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal fun ImageInCircle(
    modifier: Modifier = Modifier,
    painter: Painter,
    circleColor: Color
) {
    Image(
        painter = painter,
        modifier = modifier.drawBehind {
            drawCircle(color = circleColor)
        },
        contentDescription = null
    )
}