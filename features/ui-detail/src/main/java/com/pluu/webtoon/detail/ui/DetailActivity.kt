package com.pluu.webtoon.detail.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.animatedColor
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
import androidx.compose.runtime.Providers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.pluu.compose.transition.colorStartToEndTransition
import com.pluu.compose.transition.colorStateKey
import com.pluu.compose.transition.colorTransitionDefinition
import com.pluu.compose.ui.graphics.toColor
import com.pluu.compose.utils.ProvideDisplayInsets
import com.pluu.compose.utils.navigationBarsPadding
import com.pluu.compose.utils.statusBarsHeight
import com.pluu.compose.utils.statusBarsPadding
import com.pluu.core.utils.lazyNone
import com.pluu.utils.ProgressDialog
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.utils.observeNonNull
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.detail.model.DetailPageFieldValue
import com.pluu.webtoon.detail.model.getMessage
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.ui.compose.ActivityComposeView
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.glide.AmbientRequestManager

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

    private val dlg by lazyNone {
        ProgressDialog.create(this, R.string.msg_loading)
    }

    private val colorDefinition by lazy {
        colorTransitionDefinition(
            startColor = getThemeColor(R.attr.colorPrimary).toColor(),
            endColor = palletColor.darkVibrantColor.toColor(),
            durationMillis = 750
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val requestManager = Glide.with(this)

        ActivityComposeView {
            ProvideDisplayInsets {
                Providers(AmbientRequestManager provides requestManager) {
                    var showNavigation by remember { mutableStateOf(true) }
                    Box(modifier = Modifier.fillMaxSize()) {
                        initContentUi(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .navigationBarsPadding()
                        ) {
                            showNavigation = showNavigation.not()
                        }
                        initTopUi(
                            showNavigation,
                            Modifier.align(Alignment.TopCenter)
                        )
                        initBottomUi(
                            showNavigation,
                            Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun initTopUi(
        showNavigation: Boolean,
        modifier: Modifier = Modifier
    ) {
        val element by viewModel.elementEvent.observeAsState()

        // Animate Value
        val colorAnimation = animatedColor(
            getThemeColor(R.attr.colorPrimary).toColor()
        )

        Column(modifier = modifier) {
            Divider(
                modifier = Modifier.statusBarsHeight().zIndex(1f),
                color = colorAnimation.value,
                thickness = 0.dp
            )
            DetailTopUi(
                modifier = Modifier
                    .preferredHeight(topAppBarSize)
                    .offset(y = animate(if (showNavigation) 0.dp else -topAppBarSize)),
                title = element?.title.orEmpty(),
                subTitle = element?.webToonTitle.orEmpty(),
                backgroundColor = colorAnimation.value,
                onBackPressed = {
                    finish()
                },
                onShared = {
                    // 공유하기
                    viewModel.requestShare()
                }
            )
        }

        onCommit(colorAnimation) {
            colorAnimation.animateTo(palletColor.darkVibrantColor.toColor())
        }
    }

    @Composable
    private fun initBottomUi(
        showNavigation: Boolean,
        modifier: Modifier = Modifier
    ) {
        var page by remember {
            mutableStateOf(
                DetailPageFieldValue(
                    isPrevEnabled = false,
                    isNextEnabled = false
                )
            )
        }
        val transition = colorStartToEndTransition(colorDefinition)
        val showTransition = transition(
            definition = bottomDefinition,
            toState = showNavigation
        )

        viewModel.elementEvent.observeAsState().value?.let { element ->
            page = page.copy(
                isPrevEnabled = element.prevEpisodeId.isNullOrEmpty().not(),
                isNextEnabled = element.nextEpisodeId.isNullOrEmpty().not()
            )
        }

        DetailNavigationUi(
            modifier = modifier
                .navigationBarsPadding()
                .preferredHeight(bottomBarSize)
                .offset(y = showTransition[offset]),
            buttonBackgroundColor = transition[colorStateKey],
            isPrevEnabled = page.isPrevEnabled,
            onPrevClicked = { viewModel.movePrev() },
            isNextEnabled = page.isNextEnabled,
            onNextClicked = { viewModel.moveNext() }
        )
    }

    @Composable
    private fun initContentUi(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        val element by viewModel.list.observeAsState()

        if (element == null) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.secondary
                )
            }
        } else {
            DetailContentUi(
                modifier = modifier,
                items = element.orEmpty(),
                onClick = onClick
            )
        }
    }

    private fun initView() {
        viewModel.event.observeNonNull(this) { event ->
            when (event) {
                DetailEvent.START -> dlg.show()
                DetailEvent.LOADED -> dlg.dismiss()
                is DetailEvent.ERROR -> {
                    dlg.dismiss()
                    showError(event)
                }
                is DetailEvent.SHARE -> {
                    showShare(event.item)
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

