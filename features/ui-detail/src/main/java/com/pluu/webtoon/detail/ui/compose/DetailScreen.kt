package com.pluu.webtoon.detail.ui.compose

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.transition.ColorTransitionState
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
        backgroundColor = featureColorValue,
        contentColor = Color.White,
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
    contentColor: Color = contentColorFor(backgroundColor),
    onToggleNavigation: () -> Unit,
    onUiEvent: (DetailUiEvent) -> Unit
) {
    var topSize by rememberMutableStateOf(IntSize.Zero)
    val topOffset by animateDpAsState(
        targetValue = if (isShowNavigation) 0.dp else -topSize.height.dp,
        animationSpec = tween(animationDuration)
    )

    var bottomSize by rememberMutableStateOf(IntSize.Zero)
    val bottomOffset by animateDpAsState(
        targetValue = if (isShowNavigation) 0.dp else bottomSize.height.dp,
        animationSpec = tween(animationDuration)
    )

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
        InitTopUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size -> topSize = size }
                .offset(y = topOffset.value.dp),
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            uiStateElement = uiStateElement,
            onBackPressed = { onUiEvent(DetailUiEvent.OnBackPressed) },
            onSharedPressed = { onUiEvent(DetailUiEvent.OnSharedPressed) }
        )
        InitBottomUi(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged { size -> bottomSize = size }
                .offset(y = bottomOffset.value.dp),
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            uiStateElement = uiStateElement,
            onPrevClicked = { onUiEvent(DetailUiEvent.OnPrevPressed) }
        ) { onUiEvent(DetailUiEvent.OnNextPressed) }
    }
}

@DayNightPreview
@Composable
private fun PreviewDetailScreen_Loading() {
    AppTheme {
        DetailScreen(
            uiStateElement = UiState(loading = true),
            isShowNavigation = true,
            onToggleNavigation = {},
            onUiEvent = {}
        )
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
            onToggleNavigation = {},
            onUiEvent = {}
        )
    }
}

private const val animationDuration = 350