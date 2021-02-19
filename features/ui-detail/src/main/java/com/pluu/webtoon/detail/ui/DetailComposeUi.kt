package com.pluu.webtoon.detail.ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.DetailPageFieldValue
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight

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
            modifier = Modifier.statusBarsHeight().zIndex(1f),
            color = featureColor,
            thickness = 0.dp
        )
        DetailTopUi(
            modifier = Modifier
                .preferredHeight(topAppBarSize)
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
    var page by remember {
        mutableStateOf(
            DetailPageFieldValue(
                isPrevEnabled = false,
                isNextEnabled = false
            )
        )
    }

    val transition = updateTransition(showNavigation)
    val offsetY by transition.animateDp {
        if (it) 0.dp else bottomBarSize
    }

    DisposableEffect(page, uiStateElement) {
        page = page.copy(
            isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not(),
            isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not()
        )
        onDispose { }
    }

    DetailNavigationUi(
        modifier = modifier
            .navigationBarsPadding()
            .preferredHeight(bottomBarSize)
            .offset(y = offsetY),
        buttonBackgroundColor = featureColor,
        isPrevEnabled = page.isPrevEnabled,
        onPrevClicked = onPrevClicked,
        isNextEnabled = page.isNextEnabled,
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
