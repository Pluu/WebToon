package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.ui.compose.graphics.toColor

@Composable
fun EpisodeTopUi(
    title: String,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onFavoriteClicked: (isFavorite: Boolean) -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Filled.ArrowBack)
            }
        },
        actions = {
            IconButton(onClick = {
                onFavoriteClicked(isFavorite)
            }) {
                if (isFavorite) {
                    Icon(asset = Icons.Default.Favorite, tint = 0xFFF44336.toColor())
                } else {
                    Icon(asset = Icons.Default.FavoriteBorder)
                }
            }
        }
    )
}

@Preview(
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun previewEpisodeTopUi() {
    EpisodeTopUi(
        title = "테스트",
        isFavorite = true,
        onFavoriteClicked = {},
        onBackPressed = {}
    )
}
