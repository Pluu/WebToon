package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui_common.R
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.weekly.WeeklyDrawer
import com.pluu.webtoon.weekly.ui.weekly.WeeklyTopBar
import kotlinx.coroutines.launch

@Composable
internal fun WeeklyScreen(
    modifier: Modifier = Modifier,
    naviItem: UI_NAV_ITEM,
    onEventAction: (WeeklyMenuEvent) -> Unit,
    drawerState: DrawerState,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                Modifier.width(320.dp),
                drawerContainerColor = naviItem.bgColor,
                drawerContentColor = MaterialTheme.colorScheme.background
            ) {
                WeeklyDrawer(
                    title = stringResource(R.string.app_name),
                    menus = UI_NAV_ITEM.entries.iterator(),
                    selectedMenu = naviItem
                ) { event ->
                    onEventAction(event)
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        modifier = modifier,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                WeeklyTopBar(
                    title = stringResource(R.string.app_name),
                    backgroundColor = naviItem.bgColor,
                    contentColor = Color.White
                ) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            content = content
        )
    }
}

@DayNightPreview
@Composable
private fun PreviewWeeklyScreen() {
    AppTheme {
        WeeklyScreen(
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            naviItem = UI_NAV_ITEM.NAVER,
            onEventAction = {},
            content = {}
        )
    }
}