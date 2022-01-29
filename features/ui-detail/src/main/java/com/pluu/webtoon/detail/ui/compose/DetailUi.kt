package com.pluu.webtoon.detail.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.ProgressDialog
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.FeatureColor
import com.pluu.webtoon.detail.ui.DetailEvent
import com.pluu.webtoon.detail.ui.DetailUiEvent
import com.pluu.webtoon.detail.ui.DetailViewModel
import com.pluu.webtoon.model.ShareItem

@Composable
internal fun DetailUi(
    viewModel: DetailViewModel,
    featureColor: FeatureColor,
    errorAction: (DetailEvent.ERROR) -> Unit,
    shareAction: (ShareItem) -> Unit,
    eventAction: (DetailUiEvent) -> Unit
) {
    var loadingDialog by rememberMutableStateOf(false)
    val event by viewModel.event.observeAsState()
    val elementUiState by viewModel.elementUiState.observeAsState(
        UiState(loading = true)
    )

    when (val eventOnScope = event ?: return) {
        DetailEvent.START -> {
            loadingDialog = true
        }
        DetailEvent.LOADED -> {
            loadingDialog = false
        }
        is DetailEvent.ERROR -> {
            loadingDialog = false
            errorAction(eventOnScope)
        }
        is DetailEvent.SHARE -> {
            shareAction(eventOnScope.item)
        }
    }

    if (loadingDialog) {
        ProgressDialog(title = "Loading ...")
    }

    DetailScreen(
        featureColor = featureColor,
        uiStateElement = elementUiState,
        onUiEvent = eventAction
    )
}