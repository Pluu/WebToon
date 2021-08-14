package com.pluu.webtoon.weekly.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.webtoon.weekly.R
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import kotlinx.coroutines.launch

@Composable
fun WeeklyScreen(
    modifier: Modifier = Modifier,
    naviItem: UI_NAV_ITEM,
    backgroundColor: Color,
    onEventAction: (WeeklyMenuEvent) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(scaffoldState.drawerState.isOpen) {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    WeeklyScreen(
        modifier = modifier,
        naviItem = naviItem,
        backgroundColor = backgroundColor,
        onEventAction = onEventAction,
        scaffoldState = scaffoldState,
        content = content
    )
}

@Composable
private fun WeeklyScreen(
    modifier: Modifier = Modifier,
    naviItem: UI_NAV_ITEM,
    backgroundColor: Color,
    onEventAction: (WeeklyMenuEvent) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        drawerContent = {
            WeeklyDrawer(
                title = context.getString(R.string.app_name),
                accentColor = backgroundColor,
                menus = UI_NAV_ITEM.values().iterator(),
                selectedMenu = naviItem
            ) { event ->
                onEventAction(event)
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        },
        drawerGesturesEnabled = false,
        drawerElevation = 0.dp,
        drawerScrimColor = MaterialTheme.colors.background.copy(alpha = 0.5f),
        topBar = {
            WeeklyTopBar(
                title = context.getString(R.string.app_name),
                backgroundColor = backgroundColor
            ) {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        scaffoldState = scaffoldState,
        content = content
    )
}

@Preview(widthDp = 320, heightDp = 480)
@Composable
fun PreviewWeeklyScreen() {
    ProvideWindowInsets {
        WeeklyScreen(
            naviItem = UI_NAV_ITEM.NAVER,
            backgroundColor = Color.Blue,
            onEventAction = {},
            scaffoldState = rememberScaffoldState(
                drawerState = DrawerState(initialValue = DrawerValue.Open)
            )
        ) {
            Box {}
        }
    }
}