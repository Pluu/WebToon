package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pluu.compose.ui.graphics.toColor
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.weekly.provider.NaviColorProvider
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WeeklyContainerScreen(
    modifier: Modifier = Modifier,
    serviceApi: WeeklyUseCase,
    viewModelFactory: WeeklyViewModelFactory,
    colorProvider: NaviColorProvider
) {
    Column(modifier = modifier) {
        val coroutineScope = rememberCoroutineScope()

        val pagerState = rememberPagerState(
            initialPage = serviceApi.todayTabPosition
        )

        DayOfWeekUi(
            titles = serviceApi.currentTabs,
            selectedTabIndex = pagerState.currentPage,
            indicatorColor = colorProvider.getTitleColor().toColor()
        ) { index ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = serviceApi.currentTabs.size,
            state = pagerState
        ) { page ->
            WeeklyHomeUi(
                modifier = Modifier.fillMaxSize(),
                viewModelFactory = viewModelFactory,
                weekPosition = page
            )
        }
    }
}