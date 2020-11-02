package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pluu.compose.utils.navigationBarsHeightPlus
import com.pluu.compose.utils.statusBarsHeightPlus
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.utils.toAgentGlideUrl
import dev.chrisbanes.accompanist.glide.GlideImage
import timber.log.Timber

@Composable
fun DetailContentUi(
    modifier: Modifier = Modifier,
    items: List<DetailView>,
    onClick: () -> Unit
) {
    LazyColumnForIndexed(
        items = items,
        modifier = modifier.tapGestureFilter {
            onClick()
        }
    ) { index, item ->
        if (index == 0) {
            Spacer(Modifier.statusBarsHeightPlus(48.dp))
        }
        GlideImage(
            modifier = Modifier.fillMaxWidth()
                .sizeIn(minHeight = 10.dp),
            data = item.url.toAgentGlideUrl(),
            error = {
                Timber.e(it.throwable)
                Image(asset = vectorResource(id = R.drawable.ic_sentiment_very_dissatisfied_48))
            },
//            shouldRefetchOnSizeChange = { state, _ ->
//                state is ImageLoadState.Success
//            }
        )
        if (items.size - 1 == index) {
            Spacer(Modifier.navigationBarsHeightPlus(48.dp))
        }
    }
}