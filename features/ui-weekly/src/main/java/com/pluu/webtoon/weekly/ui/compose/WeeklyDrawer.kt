package com.pluu.webtoon.weekly.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.ServiceConst
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM

@Composable
internal fun WeeklyDrawer(
    modifier: Modifier = Modifier,
    title: String,
    menus: Iterator<UI_NAV_ITEM>,
    selectedMenu: UI_NAV_ITEM,
    onEventAction: (WeeklyMenuEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = selectedMenu.bgColor)
                .statusBarsPadding()
                .sizeIn(minHeight = 56.dp)
                .padding(horizontal = 16.dp)
                .wrapContentSize(align = Alignment.CenterStart)
        )

        Spacer(modifier = Modifier.height(8.dp))

        menus.withIndex().forEach { (index, item) ->
            val isSelected = selectedMenu == item
            Text(
                stringResource(ServiceConst.NAV_DRAWER_TITLE_RES_ID[index]),
                color = if (isSelected) {
                    selectedMenu.color
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = selectedMenu.color)
                    ) {
                        onEventAction(WeeklyMenuEvent.OnMenuClicked(item))
                    }
                    .sizeIn(minHeight = 48.dp)
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = selectedMenu.color)
                ) {
                    onEventAction(WeeklyMenuEvent.OnSettingClicked)
                }
                .sizeIn(minHeight = 48.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "설정",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@DayNightPreview
@Composable
private fun PreviewWeeklyDrawer() {
    AppTheme {
        WeeklyDrawer(
            title = "Sample",
            menus = UI_NAV_ITEM.entries.iterator(),
            selectedMenu = UI_NAV_ITEM.NAVER,
            onEventAction = {}
        )
    }
}
