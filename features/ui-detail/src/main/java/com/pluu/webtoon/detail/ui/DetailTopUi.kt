package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailTopUi(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    onBackPressed: () -> Unit,
    onShared: () -> Unit
) {
    TopAppBar(
        modifier = modifier.background(color = backgroundColor),
        contentColor = Color.White,
        backgroundColor = backgroundColor,
        title = {
            Column {
                Text(text = title, fontSize = 22.sp, maxLines = 1)
                Text(text = subTitle, fontSize = 14.sp, maxLines = 1)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        elevation = 0.dp,
        actions = {
            IconButton(onClick = onShared) {
                Icon(
                    imageVector = Icons.Default.Share,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewDetailTopUi() {
    DetailTopUi(
        title = "타이틀",
        subTitle = "서브 타이틀",
        onShared = {},
        onBackPressed = {}
    )
}