package com.pluu.compose.ui.graphics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Stable
class ImageColorState(
    var image: ImageBitmap? = null
)

@Composable
fun rememberImageState(
    state: ImageColorState = ImageColorState()
): ImageColorState = remember {
    state
}
