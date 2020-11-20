package com.pluu.webtoon.episode.compose

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset

@Composable
fun ImageInCircle(
    asset: VectorAsset,
    circleColor: Color,
    modifier: Modifier = Modifier
) {
    Image(
        asset = asset,
        modifier = modifier.drawBehind {
            drawCircle(color = circleColor)
        }
    )
}