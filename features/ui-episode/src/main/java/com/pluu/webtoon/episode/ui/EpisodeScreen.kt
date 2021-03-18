package com.pluu.webtoon.episode.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.compose.ui.graphics.toColor
import com.pluu.ui.state.UiState
import com.pluu.utils.ThemeHelper
import com.pluu.utils.getThemeColor
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

@Composable
fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    palletColor: PalletColor,
    episodeList: Result<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    firstItem: EpisodeInfo?,
    eventAction: (EpisodeUiEvent) -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(ColorTransitionState.START).apply {
            targetState = ColorTransitionState.END
        }
    }

    val transition = updateTransition(transitionState)
    val featureBgColor by animateBgColor(palletColor, transition)
    val featureTextColor by animateTestColor(palletColor, transition)

    EpisodeScreen(
        modifier = modifier,
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        featureBgColor = featureBgColor,
        featureTextColor = featureTextColor,
        episodeList = episodeList,
        readIdSet = readIdSet,
        firstItem = firstItem,
        eventAction = eventAction
    )
}

@Composable
private fun EpisodeScreen(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    featureBgColor: Color,
    featureTextColor: Color,
    episodeList: Result<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    firstItem: EpisodeInfo?,
    eventAction: (EpisodeUiEvent) -> Unit
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
                eventAction(EpisodeUiEvent.UpdateFavorite(currentFavorite.not()))
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(62.dp)
                    .padding(3.dp)
            ) {
                if (firstItem != null) {
                    EpisodeInfoUi(
                        name = webToonItem.info.writer,
                        rate = webToonItem.info.rate,
                        infoTextColor = featureTextColor,
                        buttonBackgroundColor = featureBgColor,
                        onFirstClicked = {
                            eventAction(EpisodeUiEvent.OnShowFirst(firstItem))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        EpisodeContentUi(
            modifier = Modifier.padding(innerPadding),
            uiState = convertUiState(episodeList),
            readIdSet = readIdSet,
            onMoreLoaded = { eventAction(EpisodeUiEvent.MoreLoad) }
        ) { item ->
            eventAction(EpisodeUiEvent.OnShowDetail(item))
        }
    }
}

private fun convertUiState(value: Result<List<EpisodeInfo>>): UiState<List<EpisodeInfo>> {
    return when (value) {
        is Result.Success -> UiState(
            data = value.data.takeIf { it.isNotEmpty() },
            loading = value.data.isEmpty()
        )
        is Result.Error -> UiState(exception = value.throwable)
        else -> UiState()
    }
}

@Composable
private fun animateBgColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    val context = LocalContext.current
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) }
    ) { state ->
        when (state) {
            ColorTransitionState.START -> context.getThemeColor(R.attr.colorPrimary).toColor()
            ColorTransitionState.END -> palletColor.darkVibrantColor.toColor()
        }
    }
}

@Composable
private fun animateTestColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    val context = LocalContext.current
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) }
    ) { state ->
        when (state) {
            ColorTransitionState.START -> context.getThemeColor(android.R.attr.textColorPrimary)
                .toColor()
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
fun PreviewEpisodeScreen() {
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
            eventAction = {},
            episodeList = Result.Success(emptyList()),
            readIdSet = emptySet(),
            firstItem = EpisodeInfo(
                id = "",
                toonId = "",
                title = "",
                image = ""
            )
        )
    }
}
