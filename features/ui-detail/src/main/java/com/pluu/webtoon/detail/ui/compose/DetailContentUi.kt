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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.ui.compose.theme.themeRed
import com.pluu.webtoon.ui_common.R
import com.pluu.webtoon.utils.ToonImage
import com.skydoves.landscapist.coil.CoilImageState

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
    ToonImage(
        imageUrl = { item.url },
        modifier = modifier,
        loading = {
            CircularProgressIndicator(
                modifier = modifier.size(60.dp),
                color = themeRed
            )
        },
        failure = {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(R.drawable.ic_sentiment_very_dissatisfied_48),
                contentDescription = null
            )
        },
        onImageStateChanged = {
            if (it is CoilImageState.Success) {
                val bitmap = it.imageBitmap!!
                onSuccess(item, Size(bitmap.width.toFloat(), bitmap.height.toFloat()))
            }
        }
    )
}