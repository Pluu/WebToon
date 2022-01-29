package com.pluu.webtoon.episode.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.statusBarsPadding
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun EpisodeTopUi(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    isFavorite: Boolean,
    onBackPressed: () -> Unit,
    onFavoriteClicked: (isFavorite: Boolean) -> Unit
) {
    SmallTopAppBar(
        modifier = modifier
            .background(backgroundColor)
            .statusBarsPadding(),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent
        ),
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
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEpisodeTopUi() {
    AppTheme {
        EpisodeTopUi(
            title = "테스트",
            isFavorite = true,
            onFavoriteClicked = {},
            onBackPressed = {}
        )
    }
}
