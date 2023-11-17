package com.pluu.webtoon.episode.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pluu.compose.ui.tooling.preview.DayNightWrapPreview
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun EpisodeTopUi(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    isFavorite: Boolean,
    onBackPressed: () -> Unit,
    onFavoriteClicked: (isFavorite: Boolean) -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onFavoriteClicked(isFavorite) }) {
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
        },
        colors = topAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}

internal class PreviewItemProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true,
        false
    )
    override val count: Int = values.count()
}

@DayNightWrapPreview
@Composable
private fun PreviewEpisodeTopUi(
    @PreviewParameter(PreviewItemProvider::class) isFavorite: Boolean,
) {
    AppTheme {
        EpisodeTopUi(
            title = "테스트",
            isFavorite = isFavorite,
            onFavoriteClicked = {},
            onBackPressed = {}
        )
    }
}