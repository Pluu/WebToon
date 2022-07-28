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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.WeeklyViewModel
import com.pluu.webtoon.weekly.utils.createWeeklyDayViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun WeeklyUi(
    naviItem: UI_NAV_ITEM,
    onNavigateToMenu: (UI_NAV_ITEM) -> Unit,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit,
    openSetting: () -> Unit
) {
    val selectedTabIndex =
        (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val pagerState = rememberPagerState(selectedTabIndex)
    val coroutineScope = rememberCoroutineScope()
    val viewModel: WeeklyViewModel = hiltViewModel()

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
                    openSetting()
                }
            }
        },
        drawerState = drawerState,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            DayOfWeekUi(
                selectedTabIndex = pagerState.currentPage,
                titles = viewModel.getTabs(),
                indicatorColor = naviItem.color
            ) { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                count = viewModel.getTabs().size,
                state = pagerState,
                key = { it }
            ) { page ->
                WeeklyDayUi(
                    viewModel = createWeeklyDayViewModel(
                        key = "${naviItem.name}_${page}",
                        position = page
                    ),
                    openEpisode = openEpisode
                )
            }
        }
    }
}