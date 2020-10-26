package com.pluu.webtoon.weekly.ui

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.weekly.R

@Composable
fun WeeklyListUi(
    items: List<ToonInfoWithFavorite>,
    modifier: Modifier = Modifier,
    onClicked: (ToonInfoWithFavorite) -> Unit
) {
    LazyColumnFor(
        items = items,
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 3.dp)
    ) { item ->
        WeeklyItemUi(
            item = item.info,
            isFavorite = item.isFavorite,
            onClicked = { onClicked.invoke(item) }
        )
    }
}

@Composable
fun WeeklyEmptyUi() {
    Image(asset = vectorResource(R.drawable.ic_sentiment_very_dissatisfied_48))
}

@Preview("EmptyView", widthDp = 100, heightDp = 100)
@Composable
fun previewWeeklyEmptyUi() {
    Box(
        modifier = Modifier.fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        WeeklyEmptyUi()
    }
}

@Composable
fun WeeklyLoadingUi(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 0f
) {
    CircularProgressIndicator(
        modifier = modifier.preferredWidth(60.dp).preferredHeight(60.dp),
        color = MaterialTheme.colors.secondary,
        progress = progress
    )
}

@Preview(widthDp = 100, heightDp = 100, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun previewWeeklyLoadingUi() {
    Box(
        modifier = Modifier.fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        WeeklyLoadingUi(progress = 1f)
    }
}
