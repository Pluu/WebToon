package com.pluu.webtoon.weekly.ui.weekly

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pluu.compose.ui.tooling.preview.DayNightWrapPreview
import com.pluu.webtoon.ui.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeeklyTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    title: String,
    onDrawerClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        colors = topAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}

@Composable
internal fun DayOfWeekUi(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    titles: List<String>,
    indicatorColor: Color,
    onTabSelected: (Int) -> Unit
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = backgroundColor,
        contentColor = contentColor,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    tabPositions[selectedTabIndex]
                ),
                color = indicatorColor
            )
        },
        divider = {},
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        edgePadding = 0.dp
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                text = { Text(text = title) },
                selected = index == selectedTabIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@DayNightWrapPreview
@Composable
private fun PreviewWeeklyTopBar() {
    AppTheme {
        WeeklyTopBar(
            title = "Test Title",
            onDrawerClicked = {}
        )
    }
}

@DayNightWrapPreview
@Composable
private fun PreviewDayOfWeekUi() {
    AppTheme {
        DayOfWeekUi(
            selectedTabIndex = 2,
            backgroundColor = MaterialTheme.colorScheme.surface,
            titles = (0..10).map { "$it" },
            indicatorColor = Color.Red
        ) {}
    }
}
