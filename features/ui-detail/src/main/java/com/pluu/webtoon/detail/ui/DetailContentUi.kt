package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.utils.applyAgent

@Composable
fun DetailContentUi(
    modifier: Modifier = Modifier,
    items: List<DetailView>,
    onClick: () -> Unit
) {
    val cachedRatioMap = remember { mutableStateMapOf<String, Float>() }

    BoxWithConstraints {
        LazyColumn(modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() })
            }
        ) {
            itemsIndexed(items,
                key = { _, item -> item.url }
            ) { index, item ->
                if (index == 0) {
                    Spacer(Modifier.statusBarsHeight(48.dp))
                }

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

                if (items.size - 1 == index) {
                    Spacer(Modifier.navigationBarsHeight(48.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun AdjustDetailImage(
    item: DetailView,
    modifier: Modifier = Modifier,
    onSuccess: (DetailView, Size) -> Unit
) {
    val painter = rememberImagePainter(
        data = item.url,
        builder = {
            applyAgent()
            crossfade(true)
        }
    )

    when (val state = painter.state) {
        is ImagePainter.State.Success -> {
            onSuccess(item, state.painter.intrinsicSize)
        }
        is ImagePainter.State.Loading -> {
            CircularProgressIndicator(
                modifier = modifier.size(60.dp),
                color = MaterialTheme.colors.secondary
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