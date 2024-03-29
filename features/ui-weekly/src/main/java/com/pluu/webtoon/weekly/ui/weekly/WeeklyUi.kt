package com.pluu.webtoon.weekly.ui.weekly

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.pluu.webtoon.Const
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.WeeklyScreen
import com.pluu.webtoon.weekly.ui.day.WeeklyDayUi
import com.pluu.webtoon.weekly.utils.hiltViewModelWithAdditional
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@Composable
fun WeeklyUi(
    naviItem: UI_NAV_ITEM,
    onNavigateToMenu: (UI_NAV_ITEM) -> Unit,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit,
    openSetting: () -> Unit
) {
    val viewModel: WeeklyViewModel = hiltViewModel()
    val selectedTabIndex =
        (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7

    WeeklyUi(
        naviItem = naviItem,
        tabs = viewModel.getTabs(),
        selectedTabIndex = selectedTabIndex,
        onNavigateToMenu = onNavigateToMenu,
        openEpisode = openEpisode,
        openSetting = openSetting,
    )
}

@Composable
internal fun WeeklyUi(
    naviItem: UI_NAV_ITEM,
    tabs: List<String>,
    selectedTabIndex: Int,
    onNavigateToMenu: (UI_NAV_ITEM) -> Unit,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit,
    openSetting: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabs.size }
    )
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
                    openSetting()
                }
            }
        },
        drawerState = drawerState,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                )
                .fillMaxSize()
        ) {
            DayOfWeekUi(
                selectedTabIndex = pagerState.currentPage,
                titles = tabs,
                indicatorColor = naviItem.color
            ) { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                key = { it }
            ) { page ->
                WeeklyDayUi(
                    viewModel = hiltViewModelWithAdditional(
                        key = "${naviItem.name}_${page}",
                        additionalExtras = bundleOf(Const.EXTRA_WEEKLY_POSITION to page)
                    ),
                    openEpisode = openEpisode
                )
            }
        }
    }
}