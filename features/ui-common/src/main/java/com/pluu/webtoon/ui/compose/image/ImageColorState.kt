package com.pluu.webtoon.ui.compose.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageAsset

@Stable
class ImageColorState(
    var image: ImageAsset? = null
)

@Composable
fun rememberImageState(
    state: ImageColorState = ImageColorState()
): ImageColorState = remember {
    state
}
