package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.utils.applyAgent

@Composable
internal fun DetailContentUi(
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
                    Spacer(
                        Modifier.windowInsetsTopHeight(
                            WindowInsets.statusBars.add(WindowInsets(top = 41.dp))
                        )
                    )
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

                if (index == items.lastIndex) {
                    Spacer(
                        Modifier.windowInsetsBottomHeight(
                            WindowInsets.navigationBars.add(WindowInsets(bottom = 48.dp))
                        )
                    )
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
                color = colorResource(com.pluu.webtoon.ui_common.R.color.progress_accent_color)
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