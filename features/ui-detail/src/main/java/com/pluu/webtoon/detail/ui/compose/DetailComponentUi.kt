package com.pluu.webtoon.detail.ui.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.ui.ElementEvent

@Composable
internal fun InitTopUi(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    onBackPressed: () -> Unit,
    onSharedPressed: () -> Unit
) {
    var size by rememberMutableStateOf(Size.Zero)
    val offset by animateDpAsState(
        targetValue = if (showNavigation) 0.dp else -size.height.dp,
        animationSpec = tween(animationDuration)
    )

    Column(modifier = modifier) {
        Divider(
            modifier = Modifier.zIndex(1f),
            color = backgroundColor,
            thickness = 0.dp
        )
        DetailTopUi(
            modifier = Modifier
                .onGloballyPositioned { container ->
                    if (size == Size.Zero) {
                        size = container.size.toSize()
                    }
                }
                .offset(y = offset),
            title = uiStateElement.data?.title.orEmpty(),
            subTitle = uiStateElement.data?.webToonTitle.orEmpty(),
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            onBackPressed = onBackPressed,
            onShared = onSharedPressed
        )
    }
}

@Composable
internal fun InitBottomUi(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    uiStateElement: UiState<ElementEvent>,
    showNavigation: Boolean,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    var size by rememberMutableStateOf(Size.Zero)
    val offset by animateDpAsState(
        targetValue = if (showNavigation) 0.dp else size.height.dp,
        animationSpec = tween(animationDuration)
    )

    val isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not()
    val isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not()

    DetailNavigationUi(
        modifier = modifier
            .onGloballyPositioned { container ->
                if (size == Size.Zero) {
                    size = container.size.toSize()
                }
            }
            .offset(y = offset),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
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
                color = colorResource(com.pluu.webtoon.ui_common.R.color.progress_accent_color)
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

private const val animationDuration = 350