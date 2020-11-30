package com.pluu.webtoon.ui.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabConstants
import androidx.compose.material.TabConstants.defaultTabIndicatorOffset
import androidx.compose.material.TabPosition
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.pluu.compose.utils.statusBarsPadding

@Composable
fun WeeklyTopBar(
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
                Icon(Icons.Filled.Menu)
            }
        }
    )
}

@Composable
fun DayOfWeekUi(
    titles: Array<String>,
    selectedTabIndex: Int,
    indicatorColor: Color,
    onTabSelected: (Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabConstants.DefaultIndicator(
            modifier = Modifier.defaultTabIndicatorOffset(tabPositions[selectedTabIndex]),
            color = indicatorColor,
            height = TabConstants.DefaultIndicatorHeight
        )
    }

    // TODO: 첫 화면에서 기본 선택한 Tab에 이동
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = emptyContent(),
        indicator = indicator,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        edgePadding = 0.dp
    ) {
        titles.forEachIndexed { index, title ->
            // TODO : Tab 크기 수정
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabSelected(index) }
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(vertical = 14.dp, horizontal = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun previewWeeklyTopBar() {
    WeeklyTopBar(
        backgroundColor = Color.DarkGray,
        title = "Test Title",
        onDrawerClicked = {}
    )
}

@Preview
@Composable
fun previewDayOfWeekUi() {
    DayOfWeekUi(
        titles = (0..10).map { "$it" }.toTypedArray(),
        selectedTabIndex = 2,
        indicatorColor = Color.Red,
        onTabSelected = {}
    )
}
