package com.pluu.webtoon.detail.compose

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage

@Composable
fun GlideImageAdjustBounds(
    modifier: Modifier = Modifier,
    data: Any,
    error: @Composable ((ImageLoadState.Error) -> Unit)? = null,
    loading: @Composable (() -> Unit)? = null
) {
    var heightSize by remember { mutableStateOf(1f) }
    GlideImage(
        modifier = modifier
            .height(with(LocalDensity.current) { heightSize.toDp() }),
        data = data
    ) { imageState ->
        when (imageState) {
            is ImageLoadState.Success -> {
                MaterialLoadingImage(
                    result = imageState,
                    fadeInEnabled = false,
                    contentDescription = null
                )
                heightSize = imageState.painter.intrinsicSize.height
            }
            is ImageLoadState.Error -> if (error != null) error(imageState)
            ImageLoadState.Loading -> if (loading != null) loading()
            ImageLoadState.Empty -> Unit
        }
    }
}