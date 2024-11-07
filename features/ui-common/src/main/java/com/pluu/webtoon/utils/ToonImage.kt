package com.pluu.webtoon.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.request.ImageRequest
import com.pluu.webtoon.ui.compose.theme.themeRed
import com.pluu.webtoon.ui_common.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.coil.CoilImageState

@Composable
fun ToonImage(
    imageUrl: () -> String,
    modifier: Modifier = Modifier,
    imageOptions: ImageOptions = ImageOptions(
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    ),
    onImageStateChanged: (CoilImageState) -> Unit = {},
    previewPlaceholder: Painter? = null,
    loading: @Composable (BoxScope.(imageState: CoilImageState.Loading) -> Unit)? = {
        CircularProgressIndicator(
            modifier = Modifier
                .matchParentSize()
                .wrapContentSize(),
            color = themeRed
        )
    },
    success: @Composable (BoxScope.(imageState: CoilImageState.Success, painter: Painter) -> Unit)? = null,
    failure: @Composable (BoxScope.(imageState: CoilImageState.Failure) -> Unit)? = {
        Image(
            painter = painterResource(R.drawable.ic_sentiment_very_dissatisfied_48),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = null
        )
    }
) {
    val context = LocalContext.current
    CoilImage(
        imageRequest = {
            ImageRequest.Builder(context)
                .data(imageUrl.invoke())
                .headers(userAgentHeader)
                .crossfade(true)
                .build()
        },
        modifier = modifier,
        imageOptions = imageOptions,
        onImageStateChanged = onImageStateChanged,
        previewPlaceholder = previewPlaceholder,
        loading = loading,
        success = success,
        failure = failure
    )
}