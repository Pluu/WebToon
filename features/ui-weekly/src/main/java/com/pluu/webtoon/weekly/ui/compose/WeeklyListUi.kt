package com.pluu.webtoon.weekly.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun WeeklyListUi(
    modifier: Modifier = Modifier,
    items: List<ToonInfoWithFavorite>,
    onClicked: (ToonInfoWithFavorite) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 3.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        items(
            items = items,
            key = { item -> item.id }
        ) { item ->
            WeeklyItemUi(
                item = item.info,
                isFavorite = item.isFavorite, // TODO: 에피소드에서 즐겨찾기 후 동기화 안되는 이슈
                onClicked = { onClicked.invoke(item) }
            )
        }
    }
}

@Composable
internal fun WeeklyEmptyUi() {
    Image(
        painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_sentiment_very_dissatisfied_48),
        contentDescription = null
    )
}

@Preview("EmptyView", widthDp = 100, heightDp = 100)
@Composable
private fun PreviewWeeklyEmptyUi() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            WeeklyEmptyUi()
        }
    }
}

@Composable
internal fun WeeklyLoadingUi(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(60.dp),
        color = colorResource(com.pluu.webtoon.ui_common.R.color.progress_accent_color)
    )
}

@Preview(
    widthDp = 100, heightDp = 100,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewWeeklyLoadingUi() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            WeeklyLoadingUi()
        }
    }
}
