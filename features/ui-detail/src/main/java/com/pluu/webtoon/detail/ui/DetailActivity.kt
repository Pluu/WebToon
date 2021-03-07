package com.pluu.webtoon.detail.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.compose.ui.ProgressDialog
import com.pluu.compose.ui.graphics.toColor
import com.pluu.core.utils.lazyNone
import com.pluu.ui.state.UiState
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.detail.model.FeatureColor
import com.pluu.webtoon.detail.model.getMessage
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.glide.LocalRequestManager
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding

/**
 * 상세화면 Activity
 *
 * - 상단 UI : Value Animation
 * - 하단 UI : TransitionDefinition + TransitionState
 *
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel>()

    private val palletColor by lazyNone {
        intent.getRequiredSerializableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    private val featureColor by lazyNone {
        FeatureColor(
            themeColor = getThemeColor(R.attr.colorPrimary).toColor(),
            webToonColor = palletColor.darkVibrantColor.toColor()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val requestManager = Glide.with(this)

        activityComposeView {
            ProvideWindowInsets {
                CompositionLocalProvider(LocalRequestManager provides requestManager) {
                    var loadingDialog by rememberMutableStateOf(false)
                    val event by viewModel.event.observeAsState()
                    val elementUiState by viewModel.elementUiState.observeAsState(
                        UiState(loading = true)
                    )

                    when (val eventOnScope = event) {
                        DetailEvent.START -> {
                            loadingDialog = true
                        }
                        DetailEvent.LOADED -> {
                            loadingDialog = false
                        }
                        is DetailEvent.ERROR -> {
                            loadingDialog = false
                            showError(eventOnScope)
                        }
                        is DetailEvent.SHARE -> {
                            showShare(eventOnScope.item)
                        }
                    }

                    if (loadingDialog) {
                        ProgressDialog(title = "Loading ...")
                    }

                    DetailComposeUi(
                        featureColor = featureColor,
                        uiStateElement = elementUiState,
                        onBackPressed = { finish() },
                        onSharedPressed = { viewModel.requestShare() },
                        onPrevClicked = { viewModel.movePrev() },
                        onNextClicked = { viewModel.moveNext() }
                    )
                }
            }
        }
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    // /////////////////////////////////////////////////////////////////////////
    // 추가 처리
    // /////////////////////////////////////////////////////////////////////////

    private fun showError(event: DetailEvent.ERROR) {
        AlertDialog.Builder(this@DetailActivity)
            .setMessage(event.errorType.getMessage(this))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                supportFragmentManager.findFragmentByTag(Const.DETAIL_FRAG_TAG) ?: finish()
            }
            .show()
    }

    private fun showShare(item: ShareItem) {
        startActivity(
            Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtras(
                    bundleOf(
                        Intent.EXTRA_SUBJECT to item.title,
                        Intent.EXTRA_TEXT to item.url
                    )
                )
            }, "Share")
        )
    }
}

@Composable
fun DetailComposeUi(
    featureColor: FeatureColor,
    uiStateElement: UiState<ElementEvent>,
    onBackPressed: () -> Unit,
    onSharedPressed: () -> Unit,
    onPrevClicked: () -> Unit,
    onNextClicked: () -> Unit
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

    val transition = updateTransition(transitionState)

    val featureColorValue: Color by transition.animateColor(
        transitionSpec = { tween(durationMillis = 750) }
    ) { state ->
        when (state) {
            ColorTransitionState.START -> featureColor.themeColor
            ColorTransitionState.END -> featureColor.webToonColor
        }
    }

    SideEffect {
        isFirstShow = false
        transitionState.targetState = ColorTransitionState.END
    }

    Box(modifier = Modifier.fillMaxSize()) {
        InitContentUi(
            uiStateElement = uiStateElement,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            showNavigation = showNavigation.not()
        }
        InitTopUi(
            featureColor = featureColorValue,
            uiStateElement = uiStateElement,
            showNavigation = showNavigation,
            modifier = Modifier.align(Alignment.TopCenter),
            onBackPressed = onBackPressed,
            onSharedPressed = onSharedPressed
        )
        InitBottomUi(
            featureColor = featureColorValue,
            uiStateElement = uiStateElement,
            showNavigation = showNavigation,
            modifier = Modifier.align(Alignment.BottomCenter),
            onPrevClicked = onPrevClicked,
            onNextClicked = onNextClicked
        )
    }
}
