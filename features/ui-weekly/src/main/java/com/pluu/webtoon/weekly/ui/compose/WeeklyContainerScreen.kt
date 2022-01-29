package com.pluu.webtoon.weekly.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pluu.compose.ui.graphics.toColor
import com.pluu.webtoon.model.WeekPosition
import com.pluu.webtoon.weekly.provider.NaviColorProvider
import com.pluu.webtoon.weekly.ui.WeeklyViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun WeeklyContainerScreen(
    modifier: Modifier = Modifier,
    titles: Array<String>,
    selectedTabIndex: Int,
    viewModelFactory: WeeklyViewModelFactory,
    colorProvider: NaviColorProvider
) {
    Column(modifier = modifier.fillMaxSize()) {
        val coroutineScope = rememberCoroutineScope()

        val pagerState = rememberPagerState(selectedTabIndex)

        DayOfWeekUi(
            selectedTabIndex = pagerState.currentPage,
            pagerState = pagerState,
            titles = titles,
            indicatorColor = colorProvider.getTitleColor().toColor()
        ) { index ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = titles.size,
            state = pagerState,
            key = { it }
        ) { page ->
            WeeklyHomeUi(
                modifier = Modifier.fillMaxSize(),
                viewModelFactory = viewModelFactory,
                weekPosition = WeekPosition(page)
            )
        }
    }
}