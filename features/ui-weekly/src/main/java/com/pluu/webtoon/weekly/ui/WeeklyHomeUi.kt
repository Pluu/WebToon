package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pluu.webtoon.model.ToonInfoWithFavorite

@Composable
fun WeeklyHomeUi(
    modifier: Modifier = Modifier,
    items: List<ToonInfoWithFavorite>?,
    onClick: (ToonInfoWithFavorite) -> Unit
) {
    when {
        items == null -> {
            // 초기 Loading
            Box(
                modifier = modifier.wrapContentSize(Alignment.Center)
            ) {
                WeeklyLoadingUi()
            }
        }
        items.isNotEmpty() -> {
            // 해당 요일에 웹툰이 있을 경우
            WeeklyListUi(
                modifier = modifier,
                items = items,
                onClicked = onClick
            )
        }
        else -> {
            // 해당 요일에 웹툰이 없을 경우
            Box(
                modifier = modifier.wrapContentSize(Alignment.Center)
            ) {
                WeeklyEmptyUi()
            }
        }
    }
}

