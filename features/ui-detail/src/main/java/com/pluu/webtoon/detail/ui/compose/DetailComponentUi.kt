package com.pluu.webtoon.detail.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.ui.ElementEvent

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
                color = com.pluu.webtoon.ui.compose.theme.themeRed
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
