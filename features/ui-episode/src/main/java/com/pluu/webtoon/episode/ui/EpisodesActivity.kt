package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.core.utils.lazyNone
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.episode.ui.compose.EpisodeUiEvent
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.LandingInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class EpisodesActivity : ComponentActivity() {

    private val viewModel by viewModels<EpisodeViewModel>()

    private val webToonItem by lazyNone {
        intent.getRequiredSerializableExtra<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)
    }
    private val palletColor by lazyNone {
        intent.getRequiredSerializableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    @Inject
    lateinit var detailNavigator: DetailNavigator

    @Inject
    lateinit var browserNavigator: BrowserNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent)
            }

            ProvideWindowInsets(false) {
                EpisodeUi(
                    viewModel = viewModel,
                    webToonItem = webToonItem,
                    palletColor = palletColor,
                    eventAction = ::handleAction,
                    navigationAction = ::moveDetailPage,
                    savedAction = ::savedResult
                )
            }
        }
    }

    private fun handleAction(action: EpisodeUiEvent) {
        when (action) {
            is EpisodeUiEvent.OnShowDetail -> {
                moveDetailPage(action.item)
            }
            is EpisodeUiEvent.OnShowFirst -> {
                viewModel.requestFirst()?.let {
                    moveDetailPage(it)
                } ?: toast(com.pluu.webtoon.ui_common.R.string.unknown_fail)
            }
            EpisodeUiEvent.OnBackPressed -> {
                finish()
            }
        }
    }

    private fun savedResult(event: EpisodeEvent.UPDATE_FAVORITE) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(
                Const.EXTRA_FAVORITE_EPISODE,
                FavoriteResult(event.id, event.isFavorite)
            )
        })
    }

    private fun moveDetailPage(item: EpisodeInfo) {
        if (item.isLock) {
            toast(com.pluu.webtoon.ui_common.R.string.msg_not_support)
            return
        }

        when (item.landingInfo) {
            is LandingInfo.Browser -> {
                browserNavigator.openBrowser(
                    context = this,
                    url = (item.landingInfo as LandingInfo.Browser).url,
                    toolbarColor = webToonItem.info.backgroundColor.toColorInt()
                )

            }
            LandingInfo.Detail -> {
                detailNavigator.openDetail(
                    context = this,
                    item = item,
                    palletColor = palletColor
                )
            }
        }
    }
}
