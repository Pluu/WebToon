package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.detail.compose.ImageAdjustBounds
import com.pluu.webtoon.model.DetailView
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import timber.log.Timber

@Composable
fun DetailContentUi(
    modifier: Modifier = Modifier,
    items: List<DetailView>,
    onClick: () -> Unit
) {
    val cachedViewSizeMap = mutableStateMapOf<String, IntSize>()

    LazyColumn(modifier = modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() })
        }
    ) {
        itemsIndexed(
            items = items,
            itemContent = { index, item ->
                if (index == 0) {
                    Spacer(Modifier.statusBarsHeight(48.dp))
                }
                // TODO: Adjust Image 처리 개선 해야함
                ImageAdjustBounds(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            with(LocalDensity.current) {
                                (cachedViewSizeMap[item.url]?.height ?: 1).toDp()
                            }
                        ),
                    data = item.url,
                    success = {
                        if (!cachedViewSizeMap.containsKey(item.url)) {
                            cachedViewSizeMap[item.url] = it
                        }
                    },
                    error = { error ->
                        Timber.e(error.throwable)
                        Image(
                            painterResource(R.drawable.ic_sentiment_very_dissatisfied_48),
                            contentDescription = null
                        )
                    }
                )
                if (items.size - 1 == index) {
                    Spacer(Modifier.navigationBarsHeight(48.dp))
                }
            }
        )
    }
}