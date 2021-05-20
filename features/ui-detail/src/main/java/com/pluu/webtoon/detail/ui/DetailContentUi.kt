package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.utils.toAgentGlideUrl

@Composable
fun DetailContentUi(
    modifier: Modifier = Modifier,
    items: List<DetailView>,
    onClick: () -> Unit
) {
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
            // TODO: Adjust Image 처리 개선 해야함
            DetailPhotoView(item)

            if (items.size - 1 == index) {
                Spacer(Modifier.navigationBarsHeight(48.dp))
            }
        }
    }
}

@Composable
private fun DetailPhotoView(
    item: DetailView
) {
    val painter = rememberGlidePainter(
        request = item.url.toAgentGlideUrl(),
        fadeIn = true,
        previewPlaceholder = R.drawable.ic_sentiment_very_dissatisfied_48,
    )

    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}
