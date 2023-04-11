package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pluu.webtoon.ui.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTopUi(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    colors: TopAppBarColors = topAppBarColors(
        containerColor = Color.Transparent,
        navigationIconContentColor = Color.White,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White
    ),
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
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Day Preview",
    widthDp = 280
)
@Composable
private fun PreviewDetailTopUi() {
    AppTheme {
        DetailTopUi(
            modifier = Modifier.background(color = Color(0xFF2D8400)),
            title = "타이틀",
            subTitle = "서브 타이틀",
            onShared = {},
            onBackPressed = {}
        )
    }
}