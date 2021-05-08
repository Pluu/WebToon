package com.pluu.webtoon.detail.compose

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.utils.toAgentGlideUrl

@Composable
fun ImageAdjustBounds(
    modifier: Modifier = Modifier,
    data: String,
    success: (IntSize) -> Unit,
    error: @Composable ((ImageLoadState.Error) -> Unit)? = null,
    loading: @Composable (() -> Unit)? = null
) {
    val painter = rememberGlidePainter(
        request = data.toAgentGlideUrl(),
        fadeIn = true,
        previewPlaceholder = R.drawable.ic_sentiment_very_dissatisfied_48
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
    )

    when (val imageState = painter.loadState) {
        is ImageLoadState.Loading -> {
            loading?.invoke()
        }
        is ImageLoadState.Success -> {
            val drawable = imageState.result
            success(IntSize(drawable.intrinsicWidth, drawable.intrinsicHeight))
        }
        is ImageLoadState.Error -> {
            error?.invoke(imageState)
        }
    }
}
