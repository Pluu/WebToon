package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.pluu.compose.ui.tooling.preview.DayNightWrapPreview
import com.pluu.webtoon.ui.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTopUi(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onBackPressed: () -> Unit,
    onShared: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(text = title, fontSize = 22.sp, maxLines = 1)
                Text(text = subTitle, fontSize = 14.sp, maxLines = 1)
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onShared) {
                Icon(
                    imageVector = Icons.Default.Share,
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

@DayNightWrapPreview
@Composable
private fun PreviewDetailTopUi() {
    AppTheme {
        DetailTopUi(
            title = "타이틀",
            subTitle = "서브 타이틀",
            onShared = {},
            onBackPressed = {}
        )
    }
}