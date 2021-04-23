package com.pluu.webtoon.detail.ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.pluu.ui.state.UiState

@Composable
internal fun InitTopUi(
    modifier: Modifier = Modifier,
    featureColor: Color,
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    onBackPressed: () -> Unit,
    onSharedPressed: () -> Unit
) {
    val animateOffset by animateDpAsState(if (showNavigation) 0.dp else -topAppBarSize)

    Column(modifier = modifier) {
        Divider(
            modifier = Modifier
                .statusBarsHeight()
                .zIndex(1f),
            color = featureColor,
            thickness = 0.dp
        )
        DetailTopUi(
            modifier = Modifier
                .height(topAppBarSize)
                .offset(y = animateOffset),
            title = uiStateElement.data?.title.orEmpty(),
            subTitle = uiStateElement.data?.webToonTitle.orEmpty(),
            backgroundColor = featureColor,
            onBackPressed = onBackPressed,
            onShared = onSharedPressed
        )
    }
}

@Composable
internal fun InitBottomUi(
    modifier: Modifier = Modifier,
    featureColor: Color,
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    val transition = updateTransition(
        targetState = showNavigation,
        label = null
    )
    val offsetY by transition.animateDp(label = "Y-OffSet Animation") {
        if (it) 0.dp else bottomBarSize
    }

    val isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not()
    val isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not()

    DetailNavigationUi(
        modifier = modifier
            .navigationBarsPadding()
            .height(bottomBarSize)
            .offset(y = offsetY),
        buttonBackgroundColor = featureColor,
        isPrevEnabled = isPrevEnabled,
        onPrevClicked = onPrevClicked,
        isNextEnabled = isNextEnabled,
        onNextClicked = onNextClicked
    )
}

@Composable
internal fun InitContentUi(
    modifier: Modifier = Modifier,
    uiStateElement: UiState<ElementEvent>,
    onClick: () -> Unit
) {
    if (uiStateElement.loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.secondary
            )
        }
    } else {
        DetailContentUi(
            modifier = modifier,
            items = uiStateElement.data?.list.orEmpty(),
            onClick = onClick
        )
    }
}

private val topAppBarSize = 60.dp
private val bottomBarSize = 48.dp
