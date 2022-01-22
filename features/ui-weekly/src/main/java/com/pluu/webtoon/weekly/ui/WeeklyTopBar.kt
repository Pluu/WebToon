package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@Composable
internal fun WeeklyTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    title: String,
    onDrawerClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier
            .background(color = backgroundColor)
            .statusBarsPadding(),
        contentColor = Color.White,
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DayOfWeekUi(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    titles: Array<String>,
    indicatorColor: Color,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                color = indicatorColor
            )
        },
        divider = {},
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        edgePadding = 0.dp
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = index == selectedTabIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWeeklyTopBar() {
    ProvideWindowInsets {
        WeeklyTopBar(
            backgroundColor = Color.DarkGray,
            title = "Test Title",
            onDrawerClicked = {}
        )
    }
}

@ExperimentalPagerApi
@Preview
@Composable
private fun PreviewDayOfWeekUi() {
    DayOfWeekUi(
        selectedTabIndex = 2,
        pagerState = rememberPagerState(),
        titles = (0..10).map { "$it" }.toTypedArray(),
        indicatorColor = Color.Red,
        onTabSelected = {}
    )
}
