package com.pluu.webtoon.detail.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.FeatureColor

@Composable
fun DetailScreen(
    featureColor: FeatureColor,
    uiStateElement: UiState<ElementEvent>,
    onUiEvent: (DetailUiEvent) -> Unit
) {
    var showNavigation by rememberMutableStateOf(true)
    var isFirstShow by rememberMutableStateOf(true)

    val transitionState by rememberMutableStateOf(featureColor) {
        MutableTransitionState(
            if (isFirstShow) {
                ColorTransitionState.START
            } else {
                ColorTransitionState.END
            }
        )
    }

    val transition = updateTransition(
        targetState = transitionState,
        label = null
    )

    val featureColorValue: Color by transition.animateColor(
        transitionSpec = { tween(durationMillis = 750) },
        label = "Color Animation",
    ) { state ->
        when (state.targetState) {
            ColorTransitionState.START -> featureColor.themeColor
            ColorTransitionState.END -> featureColor.webToonColor
        }
    }

    SideEffect {
        isFirstShow = false
        transitionState.targetState = ColorTransitionState.END
    }

    DetailScreen(
        uiStateElement = uiStateElement,
        isShowNavigation = showNavigation,
        featureColor = featureColorValue,
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
    featureColor: Color,
    onToggleNavigation: () -> Unit,
    onUiEvent: (DetailUiEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        InitContentUi(
            uiStateElement = uiStateElement,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            onToggleNavigation()
        }
        InitTopUi(
            featureColor = featureColor,
            uiStateElement = uiStateElement,
            showNavigation = isShowNavigation,
            modifier = Modifier.align(Alignment.TopCenter),
            onBackPressed = { onUiEvent(DetailUiEvent.OnBackPressed) },
            onSharedPressed = { onUiEvent(DetailUiEvent.OnSharedPressed) }
        )
        InitBottomUi(
            featureColor = featureColor,
            uiStateElement = uiStateElement,
            showNavigation = isShowNavigation,
            modifier = Modifier.align(Alignment.BottomCenter),
            onPrevClicked = { onUiEvent(DetailUiEvent.OnPrevPressed) },
            onNextClicked = { onUiEvent(DetailUiEvent.OnNextPressed) }
        )
    }
}

@Preview(name = "Loading", heightDp = 340)
@Composable
fun PreviewDetailScreen_Loading() {
    ProvideWindowInsets {
        DetailScreen(
            uiStateElement = UiState(loading = true),
            isShowNavigation = true,
            featureColor = Color.Black,
            onToggleNavigation = {},
            onUiEvent = {}
        )
    }
}

@Preview(name = "Show", heightDp = 340)
@Composable
fun PreviewDetailScreen_Show() {
    ProvideWindowInsets {
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
            featureColor = Color.Black,
            onToggleNavigation = {},
            onUiEvent = {}
        )
    }
}