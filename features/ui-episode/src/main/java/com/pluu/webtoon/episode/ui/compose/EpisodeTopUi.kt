package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.background
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun EpisodeTopUi(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    isFavorite: Boolean,
    onBackPressed: () -> Unit,
    onFavoriteClicked: (isFavorite: Boolean) -> Unit
) {
    TopAppBar(
        modifier = modifier
            .background(color = backgroundColor)
            .statusBarsPadding(),
        contentColor = Color.White,
        backgroundColor = backgroundColor,
        title = {
            Text(text = title)
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
            IconButton(onClick = {
                onFavoriteClicked(isFavorite)
            }) {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        tint = Color(0xFFF44336),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewEpisodeTopUi() {
    ProvideWindowInsets {
        EpisodeTopUi(
            title = "테스트",
            isFavorite = true,
            onFavoriteClicked = {},
            onBackPressed = {}
        )
    }
}
