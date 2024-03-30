package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.utils.applyAgent

@Composable
internal fun DetailContentUi(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    items: List<DetailView>,
    onClick: () -> Unit
) {
    val cachedRatioMap = remember { mutableStateMapOf<String, Float>() }

    BoxWithConstraints {
        LazyColumn(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onClick() })
                },
            contentPadding = contentPadding
        ) {
            items(
                items = items,
                key = { item -> item.url }
            ) { item ->
                AdjustDetailImage(
                    item = item,
                    modifier = if (cachedRatioMap.contains(item.url)) {
                        Modifier.aspectRatio(cachedRatioMap.getValue(item.url))
                    } else {
                        Modifier.size(maxWidth, maxHeight)
                    }
                ) { detailItem, size ->
                    cachedRatioMap[detailItem.url] = size.width / size.height
                }
            }
        }
    }
}

@Composable
private fun AdjustDetailImage(
    item: DetailView,
    modifier: Modifier = Modifier,
    onSuccess: (DetailView, Size) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(item.url)
            .applyAgent()
            .crossfade(true)
            .build()
    )

    when (val state = painter.state) {
        is AsyncImagePainter.State.Success -> {
            onSuccess(item, state.painter.intrinsicSize)
        }

        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator(
                modifier = modifier.size(60.dp),
                color = com.pluu.webtoon.ui.compose.theme.themeRed
            )
        }

        else -> {}
    }

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = null
    )
}