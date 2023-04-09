package com.pluu.webtoon.detail.ui.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ShareCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.ProgressDialog
import com.pluu.ui.state.UiState
import com.pluu.webtoon.detail.model.FeatureColor
import com.pluu.webtoon.detail.model.getMessage
import com.pluu.webtoon.detail.ui.DetailEvent
import com.pluu.webtoon.detail.ui.DetailUiEvent
import com.pluu.webtoon.detail.ui.DetailViewModel
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.ui.model.PalletColor

@Composable
fun DetailUi(
    palletColor: PalletColor,
    closeCurrent: () -> Unit
) {
    val context = LocalContext.current
    var errorType: ERROR_TYPE? by rememberMutableStateOf(null)
    DetailUi(
        viewModel = hiltViewModel(),
        featureColor = FeatureColor(
            themeColor = palletColor.darkMutedColor,
            webToonColor = palletColor.darkVibrantColor
        ),
        closeCurrent = { closeCurrent() },
        shareAction = { item ->
            val intent = ShareCompat.IntentBuilder(context)
                .setChooserTitle("Share")
                .setType("text/plain")
                .setSubject(item.title)
                .setText(item.url)
                .intent
            context.startActivity(intent)
        },
        errorAction = { event ->
            errorType = event.errorType
        }
    )

    val error = errorType
    if (error != null) {
        AlertDialog(
            text = {
                Text(error.getMessage(context))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorType = null
                        closeCurrent()
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {}
        )
    }
}

@Composable
internal fun DetailUi(
    viewModel: DetailViewModel,
    featureColor: FeatureColor,
    closeCurrent: () -> Unit,
    shareAction: (ShareItem) -> Unit,
    errorAction: (DetailEvent.ERROR) -> Unit
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
        onUiEvent = {
            when (it) {
                DetailUiEvent.OnBackPressed -> closeCurrent()
                DetailUiEvent.OnNextPressed -> viewModel.moveNext()
                DetailUiEvent.OnPrevPressed -> viewModel.movePrev()
                DetailUiEvent.OnSharedPressed -> viewModel.requestShare()
            }
        }
    )
}