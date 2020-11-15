package com.pluu.webtoon.ui.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.ui.tooling.preview.Preview
import com.pluu.compose.ui.graphics.toColor
import com.pluu.compose.utils.navigationBarsPadding
import com.pluu.webtoon.model.ServiceConst
import com.pluu.webtoon.model.UI_NAV_ITEM

@Composable
fun WeeklyDrawer(
    title: String,
    menus: Iterator<UI_NAV_ITEM>,
    selectedMenu: UI_NAV_ITEM,
    modifier: Modifier = Modifier,
    onMenuClicked: (UI_NAV_ITEM) -> Unit,
    onSettingClicked: () -> Unit
) {
    val context = ContextAmbient.current
    Column(modifier = modifier.navigationBarsPadding()) {
        Text(
            title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ContextCompat.getColor(context, selectedMenu.bgColor).toColor())
                .sizeIn(minHeight = 56.dp)
                .padding(horizontal = 16.dp)
                .wrapContentSize(align = Alignment.CenterStart)
        )

        Spacer(modifier = Modifier.height(8.dp))

        menus.withIndex().forEach { (index, item) ->
            val isSelected = selectedMenu == item
            Text(
                context.getString(ServiceConst.NAVDRAWER_TITLE_RES_ID[index]),
                color = if (isSelected) {
                    ContextCompat.getColor(context, item.color).toColor()
                } else {
                    MaterialTheme.colors.onSurface
                },
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 48.dp)
                    .clickable { onMenuClicked(item) }
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 48.dp)
                .clickable(onClick = onSettingClicked)
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                asset = Icons.Default.Settings,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(Modifier.width(32.dp))
            Text(
                text = "설정",
                color = MaterialTheme.colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(
    widthDp = 320, heightDp = 450,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun previewWeeklyDrawer() {
    WeeklyDrawer(
        title = "Sample",
        menus = UI_NAV_ITEM.values().iterator(),
        selectedMenu = UI_NAV_ITEM.NAVER,
        modifier = Modifier.width(240.dp),
        onMenuClicked = {},
        onSettingClicked = {}
    )
}
