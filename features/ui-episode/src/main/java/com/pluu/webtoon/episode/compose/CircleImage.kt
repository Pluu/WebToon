package com.pluu.webtoon.episode.compose

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ImageInCircle(
    imageVector: ImageVector,
    circleColor: Color,
    modifier: Modifier = Modifier
) {
    Image(
        imageVector = imageVector,
        modifier = modifier.drawBehind {
            drawCircle(color = circleColor)
        }
    )
}