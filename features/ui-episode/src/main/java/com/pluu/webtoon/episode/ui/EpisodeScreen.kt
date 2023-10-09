package com.pluu.webtoon.episode.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.webtoon.episode.ui.compose.EpisodeInfoUi
import com.pluu.webtoon.episode.ui.compose.EpisodeTopUi
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui.model.PalletColor

@Composable
internal fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    palletColor: PalletColor,
    isFirstLoaded: Boolean,
    updateFavoriteAction: (Boolean) -> Unit,
    eventAction: (EpisodeUiEvent) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(ColorTransitionState.START).apply {
            targetState = ColorTransitionState.END
        }
    }

    val transition = updateTransition(
        transitionState = transitionState,
        label = null
    )
    val featureBgColor by transition.animateColor(
        label = "BgColor Animation"
    ) { state ->
        when (state) {
            ColorTransitionState.START -> Color(palletColor.darkMutedColor)
            ColorTransitionState.END -> Color(palletColor.darkVibrantColor)
        }
    }

    EpisodeScreen(
        modifier = modifier,
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        featureBgColor = featureBgColor,
        isFirstLoaded = isFirstLoaded,
        updateFavoriteAction = updateFavoriteAction,
        eventAction = eventAction,
        content = content
    )
}

@Composable
private fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    featureBgColor: Color,
    isFirstLoaded: Boolean,
    updateFavoriteAction: (Boolean) -> Unit,
    eventAction: (EpisodeUiEvent) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EpisodeTopUi(
                modifier = Modifier,
                title = webToonItem.info.title,
                isFavorite = isFavorite,
                backgroundColor = featureBgColor,
                contentColor = Color.White,
                onBackPressed = { eventAction(EpisodeUiEvent.OnBackPressed) }
            ) { currentFavorite ->
                updateFavoriteAction(currentFavorite.not())
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(featureBgColor)
                    .navigationBarsPadding()
                    .height(62.dp)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                if (isFirstLoaded) {
                    EpisodeInfoUi(
                        name = webToonItem.info.writer,
                        rate = webToonItem.info.rate,
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White,
                        onFirstClicked = {
                            eventAction(EpisodeUiEvent.OnShowFirst)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@DayNightPreview
@Composable
private fun PreviewEpisodeScreen() {
    AppTheme {
        EpisodeScreen(
            webToonItem = ToonInfoWithFavorite(
                ToonInfo(
                    id = "",
                    title = "타이틀 타이틀",
                    image = "",
                    updateDate = "1234.56.78",
                    status = Status.BREAK,
                    isLocked = true,
                    writer = "테스터",
                    rate = 1.1
                ), true
            ),
            isFavorite = true,
            featureBgColor = Color.Black,
            isFirstLoaded = true,
            updateFavoriteAction = {},
            eventAction = {}
        ) { }
    }
}
