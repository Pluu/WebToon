package com.pluu.webtoon.detail.ui

import androidx.compose.animation.DpPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pluu.compose.utils.navigationBarsPadding
import com.pluu.compose.utils.statusBarsHeight
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.DetailPageFieldValue

@Composable
internal fun initTopUi(
    featureColor: Color,
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onSharedPressed: () -> Unit
) {
    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.statusBarsHeight().zIndex(1f),
            color = featureColor,
            thickness = 0.dp
        )
        DetailTopUi(
            modifier = Modifier
                .preferredHeight(topAppBarSize)
                .offset(y = animate(if (showNavigation) 0.dp else -topAppBarSize)),
            title = uiStateElement.data?.title.orEmpty(),
            subTitle = uiStateElement.data?.webToonTitle.orEmpty(),
            backgroundColor = featureColor,
            onBackPressed = onBackPressed,
            onShared = onSharedPressed
        )
    }
}

@Composable
internal fun initBottomUi(
    featureColor: Color,
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    modifier: Modifier = Modifier,
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

    val showTransition = transition(
        definition = bottomDefinition,
        toState = showNavigation
    )

    onCommit(page, uiStateElement) {
        page = page.copy(
            isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not(),
            isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not()
        )
    }

    DetailNavigationUi(
        modifier = modifier
            .navigationBarsPadding()
            .preferredHeight(bottomBarSize)
            .offset(y = showTransition[offset]),
        buttonBackgroundColor = featureColor,
        isPrevEnabled = page.isPrevEnabled,
        onPrevClicked = onPrevClicked,
        isNextEnabled = page.isNextEnabled,
        onNextClicked = onNextClicked
    )
}

@Composable
internal fun initContentUi(
    uiStateElement: UiState<ElementEvent>,
    modifier: Modifier = Modifier,
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

private val offset = DpPropKey("Offset")

private val bottomDefinition = transitionDefinition<Boolean> {
    state(true) {
        this[offset] = 0.dp
    }
    state(false) {
        this[offset] = bottomBarSize
    }
}
