package com.pluu.webtoon.weekly.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pluu.webtoon.model.WeekPosition
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.WeeklyDayViewModel
import com.pluu.webtoon.weekly.ui.WeeklyDayViewModelFactory
import com.pluu.webtoon.weekly.ui.WeeklyViewModel
import com.pluu.webtoon.weekly.utils.viewModelProviderFactoryOf
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeeklyUi(
    naviItem: UI_NAV_ITEM,
    tabViewModel: WeeklyViewModel = hiltViewModel(), // TODO: 변경되지 않는 이슈
    dayViewModelFactory: WeeklyDayViewModelFactory,
    onNavigateToMenu: (UI_NAV_ITEM) -> Unit,
    onNavigateToSetting: () -> Unit
) {
    val todayTabPosition =
        (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    WeeklyScreen(
        naviItem = naviItem,
        onEventAction = { event ->
            when (event) {
                is WeeklyMenuEvent.OnMenuClicked -> {
                    onNavigateToMenu(event.item)
                }
                WeeklyMenuEvent.OnSettingClicked -> {
                    onNavigateToSetting()
                }
            }
        },
        drawerState = drawerState,
    ) { innerPadding ->
        WeeklyUi(
            modifier = Modifier.padding(innerPadding),
            titles = tabViewModel.getTabs(),
            selectedTabIndex = todayTabPosition,
            viewModelFactory = { weekPosition ->
                viewModel(
                    key = "${naviItem.name}_${weekPosition}",
                    factory = viewModelProviderFactoryOf {
                        dayViewModelFactory.create(weekPosition.value)
                    }
                )
            },
            featureColor = naviItem.color
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun WeeklyUi(
    modifier: Modifier = Modifier,
    titles: Array<String>,
    selectedTabIndex: Int,
    viewModelFactory: @Composable (WeekPosition) -> WeeklyDayViewModel,
    featureColor: Color
) {
    Column(modifier = modifier.fillMaxSize()) {
        val coroutineScope = rememberCoroutineScope()

        val pagerState = rememberPagerState(selectedTabIndex)

        DayOfWeekUi(
            selectedTabIndex = pagerState.currentPage,
            pagerState = pagerState,
            titles = titles,
            indicatorColor = featureColor
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
            val weekPosition = WeekPosition(page)
            WeeklyDayUi(
                viewModel = viewModelFactory(weekPosition)
            )
        }
    }
}
