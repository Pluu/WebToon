package com.pluu.webtoon.episode.ui.compose

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.compose.ui.graphics.toColor
import com.pluu.compose.ui.res.colorAttribute
import com.pluu.utils.ThemeHelper
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor

@Composable
internal fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    palletColor: PalletColor,
    isFirstLoded: Boolean,
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
    val featureBgColor by animateBgColor(palletColor, transition)
    val featureTextColor by animateTextColor(palletColor, transition)

    EpisodeScreen(
        modifier = modifier,
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        featureBgColor = featureBgColor,
        featureTextColor = featureTextColor,
        isFirstLoded = isFirstLoded,
        updateFavoriteAction = updateFavoriteAction,
        eventAction = eventAction,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    featureBgColor: Color,
    featureTextColor: Color,
    isFirstLoded: Boolean,
    updateFavoriteAction: (Boolean) -> Unit,
    eventAction: (EpisodeUiEvent) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EpisodeTopUi(
                title = webToonItem.info.title,
                isFavorite = isFavorite,
                backgroundColor = featureBgColor,
                onBackPressed = { eventAction(EpisodeUiEvent.OnBackPressed) }
            ) { currentFavorite ->
                updateFavoriteAction(currentFavorite.not())
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(62.dp)
                    .padding(3.dp)
            ) {
                if (isFirstLoded) {
                    EpisodeInfoUi(
                        name = webToonItem.info.writer,
                        rate = webToonItem.info.rate,
                        infoTextColor = featureTextColor,
                        buttonBackgroundColor = featureBgColor,
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

@Composable
private fun animateBgColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "BgColor Animation"
    ) { state ->
        when (state) {
            ColorTransitionState.START -> MaterialTheme.colorScheme.primary
            ColorTransitionState.END -> palletColor.darkVibrantColor.toColor()
        }
    }
}

@Composable
private fun animateTextColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    val context = LocalContext.current
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "TextColor Animation"
    ) { state ->
        when (state) {
            ColorTransitionState.START -> colorAttribute(android.R.attr.textColorPrimary).toColor()
            ColorTransitionState.END -> if (ThemeHelper.isLightTheme(context)) {
                palletColor.darkMutedColor.toColor()
            } else {
                palletColor.lightMutedColor.toColor()
            }
        }
    }
}

@Preview(heightDp = 480)
@Composable
private fun PreviewEpisodeScreen() {
    ProvideWindowInsets {
        EpisodeScreen(
            webToonItem = ToonInfoWithFavorite(
                ToonInfo(
                    id = "",
                    title = "타이틀 타이틀",
                    image = "",
                    updateDate = "1234.56.78",
                    status = Status.BREAK,
                    isAdult = true,
                    writer = "테스터",
                    rate = 1.1
                ), true
            ),
            isFavorite = true,
            featureBgColor = Color.Black,
            featureTextColor = Color.Black,
            updateFavoriteAction = {},
            eventAction = {},
            isFirstLoded = true
        ) { }
    }
}
