package com.pluu.webtoon.ui.weekly

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.R
import com.pluu.webtoon.model.UI_NAV_ITEM
import kotlinx.coroutines.launch

@Composable
fun WeeklyScreen(
    naviItem: UI_NAV_ITEM,
    backgroundColor: Color,
    onEventAction: (WeeklyEvent) -> Unit,
    bodyContent: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(scaffoldState.drawerState.isOpen) {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    Scaffold(
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
        scaffoldState = scaffoldState
    ) { innerPadding ->
        bodyContent(innerPadding)
    }
}