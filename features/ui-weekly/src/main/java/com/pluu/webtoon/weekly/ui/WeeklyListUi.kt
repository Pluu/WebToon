package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsHeight
import com.pluu.webtoon.model.ToonInfoWithFavorite

@Composable
fun WeeklyListUi(
    modifier: Modifier = Modifier,
    items: List<ToonInfoWithFavorite>,
    onClicked: (ToonInfoWithFavorite) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 3.dp)
    ) {
        itemsIndexed(items, key = { _, item ->
            item.info.id
        }) { index, item ->
            WeeklyItemUi(
                item = item.info,
                isFavorite = item.isFavorite,
                onClicked = { onClicked.invoke(item) }
            )

            if (index == items.lastIndex) {
                Spacer(Modifier.navigationBarsHeight())
            }
        }
    }
}

@Composable
fun WeeklyEmptyUi() {
    Image(
        painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_sentiment_very_dissatisfied_48),
        contentDescription = null
    )
}

@Preview("EmptyView", widthDp = 100, heightDp = 100)
@Composable
fun PreviewWeeklyEmptyUi() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        WeeklyEmptyUi()
    }
}

@Composable
fun WeeklyLoadingUi(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(60.dp),
        color = MaterialTheme.colors.secondary
    )
}

@Preview(
    widthDp = 100, heightDp = 100,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewWeeklyLoadingUi() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        WeeklyLoadingUi()
    }
}
