package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.ui.ElementEvent

@Composable
internal fun InitTopUi(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    uiStateElement: UiState<ElementEvent>,
    onBackPressed: () -> Unit,
    onSharedPressed: () -> Unit
) {
    DetailTopUi(
        modifier = modifier,
        title = uiStateElement.data?.title.orEmpty(),
        subTitle = uiStateElement.data?.webToonTitle.orEmpty(),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        onBackPressed = onBackPressed,
        onShared = onSharedPressed
    )
}

@Composable
internal fun InitBottomUi(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    uiStateElement: UiState<ElementEvent>,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    val isPrevEnabled = uiStateElement.data?.prevEpisodeId.isNullOrEmpty().not()
    val isNextEnabled = uiStateElement.data?.nextEpisodeId.isNullOrEmpty().not()

    DetailNavigationUi(
        modifier = modifier,
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
    contentPadding: PaddingValues = PaddingValues(0.dp),
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
            contentPadding = contentPadding,
            items = uiStateElement.data?.list.orEmpty(),
            onClick = onClick
        )
    }
}
