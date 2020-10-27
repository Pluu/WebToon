package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pluu.webtoon.model.ToonInfoWithFavorite

@Composable
fun WeeklyHomeUi(
    items: List<ToonInfoWithFavorite>?,
    modifier: Modifier = Modifier,
    onClick: (ToonInfoWithFavorite) -> Unit
) {
    when {
        items == null -> {
            // 초기 Loading
            Box(
                modifier = modifier.fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                WeeklyLoadingUi()
            }
        }
        items.isNotEmpty() -> {
            // 해당 요일에 웹툰이 있을 경
            // TODO: Non-Null 체크가 정상으로 되면 수정해야함
            WeeklyListUi(
                items = items,
                modifier = modifier.fillMaxWidth(),
                onClicked = onClick
            )
        }
        else -> {
            // 해당 요일에 웹툰이 없을 경우
            Box(
                modifier = modifier.fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                WeeklyEmptyUi()
            }
        }
    }
}

