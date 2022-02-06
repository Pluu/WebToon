package com.pluu.webtoon.weekly.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun WeeklyTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    title: String,
    onDrawerClicked: () -> Unit
) {
    SmallTopAppBar(
        modifier = modifier
            .background(backgroundColor)
            .statusBarsPadding(),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
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
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    pagerState: PagerState,
    titles: Array<String>,
    indicatorColor: Color,
    onTabSelected: (Int) -> Unit
) {
    // TODO: ScrollableTabRow은 M2 기반이므로 추후 수정
    Surface(
        color = backgroundColor,
        modifier = modifier.fillMaxWidth()
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = backgroundColor,
            contentColor = indicatorColor,
//        indicator = { tabPositions ->
//            TabRowDefaults.Indicator(
//                modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
//                color = indicatorColor
//            )
//        },
            divider = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            edgePadding = 0.dp
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(text = title)
                    },
                    selected = index == selectedTabIndex,
                    onClick = { onTabSelected(index) }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewWeeklyTopBar() {
    AppTheme {
        WeeklyTopBar(
            title = "Test Title",
            onDrawerClicked = {}
        )
    }
}

@ExperimentalPagerApi
@Preview(name = "Light Theme")
@Preview(
    name = "Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewDayOfWeekUi() {
    AppTheme {
        DayOfWeekUi(
            selectedTabIndex = 2,
            pagerState = rememberPagerState(),
            backgroundColor = MaterialTheme.colorScheme.surface,
            indicatorColor = Color.Red,
            titles = (0..10).map { "$it" }.toTypedArray(),
            onTabSelected = {}
        )
    }
}
