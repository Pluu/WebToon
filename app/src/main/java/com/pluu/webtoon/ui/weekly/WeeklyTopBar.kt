package com.pluu.webtoon.ui.weekly

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    title: String,
    onDrawerClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
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