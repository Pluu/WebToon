package com.pluu.webtoon.detail.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.FeatureColor
import com.pluu.webtoon.detail.ui.DetailUiEvent
import com.pluu.webtoon.detail.ui.ElementEvent
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun DetailScreen(
    featureColor: FeatureColor,
    uiStateElement: UiState<ElementEvent>,
    onUiEvent: (DetailUiEvent) -> Unit
) {
    var showNavigation by rememberMutableStateOf(true)
    var isFirstShow by rememberMutableStateOf(true)
    val transition = updateTransition(
        targetState = isFirstShow,
        label = null
    )
    val featureColorValue: Color by transition.animateColor(
        transitionSpec = { tween(durationMillis = 750) },
        label = "Color Animation",
    ) { state ->
        when (state) {
            true -> featureColor.themeColor
            false -> featureColor.webToonColor
        }
    }

    SideEffect {
        isFirstShow = false
    }

    DetailScreen(
        uiStateElement = uiStateElement,
        isShowNavigation = true,
        backgroundColor = featureColorValue,
        onToggleNavigation = {
            showNavigation = showNavigation.not()
        },
        onUiEvent = onUiEvent
    )
}

@Composable
private fun DetailScreen(
    uiStateElement: UiState<ElementEvent>,
    isShowNavigation: Boolean,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onToggleNavigation: () -> Unit,
    onUiEvent: (DetailUiEvent) -> Unit
) {
    var topSize by rememberMutableStateOf(IntSize.Zero)
    var bottomSize by rememberMutableStateOf(IntSize.Zero)

    Box(modifier = Modifier.fillMaxSize()) {
        InitContentUi(
            uiStateElement = uiStateElement,
            modifier = Modifier.fillMaxSize(),
            contentPadding = WindowInsets.safeDrawing
                .add(
                    WindowInsets(
                        top = topSize.height,
                        bottom = bottomSize.height
                    )
                )
                .asPaddingValues(),
        ) {
            onToggleNavigation()
        }

        AnimatedVisibility(
            isShowNavigation,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size -> topSize = size },
            enter = slideInVertically(tween(animationDuration)) { fullHeight ->
                -fullHeight
            },
            exit = slideOutVertically(tween(animationDuration)) { fullHeight ->
                -fullHeight
            },
        ) {
            DetailTopUi(
                modifier = Modifier.background(backgroundColor),
                title = uiStateElement.data?.title.orEmpty(),
                subTitle = uiStateElement.data?.webToonTitle.orEmpty(),
                onBackPressed = {
                    onUiEvent(DetailUiEvent.OnBackPressed)
                },
                onShared = {
                    onUiEvent(DetailUiEvent.OnSharedPressed)
                }
            )
        }

        AnimatedVisibility(
            isShowNavigation,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged { size -> bottomSize = size },
            enter = slideInVertically(tween(animationDuration)) { fullHeight ->
                fullHeight
            },
            exit = slideOutVertically(tween(animationDuration)) { fullHeight ->
                fullHeight
            },
        ) {
            DetailNavigationUi(
                modifier = Modifier.background(backgroundColor),
                isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not(),
                onPrevClicked = {
                    onUiEvent(DetailUiEvent.OnPrevPressed)
                },
                isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not(),
                onNextClicked = {
                    onUiEvent(DetailUiEvent.OnNextPressed)
                }
            )
        }
    }
}

@DayNightPreview
@Composable
private fun PreviewDetailScreen_Loading() {
    AppTheme {
        DetailScreen(
            uiStateElement = UiState(loading = true),
            isShowNavigation = true,
            onToggleNavigation = {}
        ) {}
    }
}

@DayNightPreview
@Composable
private fun PreviewDetailScreen_Show() {
    AppTheme {
        DetailScreen(
            uiStateElement = UiState(
                data = ElementEvent(
                    title = "테스트",
                    webToonTitle = "웹툰 타이틀",
                    prevEpisodeId = "1",
                    nextEpisodeId = "3",
                    list = emptyList()
                )
            ),
            isShowNavigation = true,
            onToggleNavigation = {}
        ) {}
    }
}

private const val animationDuration = 350