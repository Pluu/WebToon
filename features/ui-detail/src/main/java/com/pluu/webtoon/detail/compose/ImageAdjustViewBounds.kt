package com.pluu.webtoon.detail.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import com.pluu.webtoon.utils.toAgentGlideUrl
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage

@Composable
fun ImageAdjustBounds(
    modifier: Modifier = Modifier,
    data: String,
    success: (IntSize) -> Unit,
    error: @Composable ((ImageLoadState.Error) -> Unit)? = null,
    loading: @Composable (() -> Unit)? = null
) {
    GlideImage(
        modifier = modifier,
        data = data.toAgentGlideUrl()
    ) { imageState ->
        when (imageState) {
            is ImageLoadState.Success -> {
                MaterialLoadingImage(
                    result = imageState,
                    fadeInEnabled = false,
                    contentDescription = null
                )
                success(imageState.painter.toIntrinsicSize())
            }
            is ImageLoadState.Error -> if (error != null) error(imageState)
            ImageLoadState.Loading -> if (loading != null) loading()
            ImageLoadState.Empty -> Unit
        }
    }
}

private fun Painter.toIntrinsicSize(): IntSize {
    val size = intrinsicSize
    return IntSize(size.width.toInt(), size.height.toInt())
}
