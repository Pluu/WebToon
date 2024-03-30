package com.pluu.webtoon.weekly.ui.day

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.utils.toast
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.ui_common.R
import com.pluu.webtoon.weekly.event.WeeklyEvent
import com.pluu.webtoon.weekly.image.PalletDarkCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
internal fun WeeklyDayUi(
    modifier: Modifier = Modifier,
    viewModel: WeeklyDayViewModel,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit
) {
    val list by viewModel.listEvent.observeAsState(null)
    val event by viewModel.event.observeAsState()

    val context = LocalContext.current
    val palletCalculator = PalletDarkCalculator(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(event) {
        when (val safeEvent = event ?: return@LaunchedEffect) {
            is WeeklyEvent.ErrorEvent -> {
                context.toast(safeEvent.message)
            }
        }
    }

    WeeklyDayUi(
        modifier = modifier.fillMaxSize(),
        items = list,
    ) { item ->
        if (item.info.isLocked) {
            context.toast(R.string.msg_not_support)
        } else {
            coroutineScope.launch {
                val palletColor = async(Dispatchers.Default) {
                    palletCalculator.calculateSwatchesInImage(item.info.image)
                }
                openEpisode(item, palletColor.await())
            }
        }
    }
}

@Composable
private fun WeeklyDayUi(
    modifier: Modifier = Modifier,
    items: List<ToonInfoWithFavorite>?,
    onClick: (ToonInfoWithFavorite) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 3.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        when {
            items == null -> {
                // 초기 Loading
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.5f)
                            .wrapContentSize(Alignment.BottomCenter)
                    ) {
                        WeeklyLoadingUi()
                    }
                }
            }

            items.isNotEmpty() -> {
                items(
                    items = items,
                    key = { item -> item.id }
                ) { item ->
                    WeeklyItemUi(
                        item = item.info,
                        isFavorite = item.isFavorite, // TODO: 에피소드에서 즐겨찾기 후 동기화 안되는 이슈
                        onClicked = {
                            onClick.invoke(item)
                        }
                    )
                }
            }

            else -> {
                // 해당 요일에 웹툰이 없을 경우
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.5f)
                            .wrapContentSize(Alignment.BottomCenter)
                    ) {
                        WeeklyEmptyUi()
                    }
                }
            }
        }
    }
}

@Composable
internal fun WeeklyEmptyUi() {
    Image(
        painterResource(R.drawable.ic_sentiment_very_dissatisfied_48),
        contentDescription = null
    )
}

@Preview(
    "EmptyView",
    widthDp = 100, heightDp = 100,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewWeeklyEmptyUi() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
        color = com.pluu.webtoon.ui.compose.theme.themeRed
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
                .background(Color.White)
                .wrapContentSize(Alignment.Center)
        ) {
            WeeklyLoadingUi()
        }
    }
}
