package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.ProgressDialog
import com.pluu.core.utils.lazyNone
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.result.registerForActivityResult
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.compose.EpisodeScreen
import com.pluu.webtoon.episode.ui.compose.EpisodeUiEvent
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.LandingInfo
import com.pluu.webtoon.model.Result
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

    private val openDetailLauncher = registerForActivityResult {
        viewModel.readUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            ProvideWindowInsets {
                var showDialog by rememberMutableStateOf(false)

                var firstItem by rememberMutableStateOf<EpisodeInfo?>(null)

                val episodeList by viewModel.listEvent.observeAsState(
                    Result.Success(emptyList())
                )
                val event by viewModel.event.observeAsState(null)

                val readIdSet by viewModel.readIdSet.observeAsState(emptySet())

                val isFavorite by viewModel.favorite.observeAsState(false)

                when (val _event = event) {
                    is EpisodeEvent.START -> {
                        showDialog = _event.isOverFirstPage
                    }
                    is EpisodeEvent.LOADED -> {
                        showDialog = false
                    }
                    is EpisodeEvent.FIRST -> {
                        moveDetailPage(_event.firstEpisode)
                    }
                    is EpisodeEvent.UPDATE_FAVORITE -> {
                        updateFavorite(_event.isFavorite)
                        savedResult(_event)
                    }
                    else -> {}
                }

                if (episodeList is Result.Error) {
                    toast(com.pluu.webtoon.ui_common.R.string.network_fail)
                    finish()
                    return@ProvideWindowInsets
                }

                if (showDialog) {
                    ProgressDialog(title = "Loading...")
                }

                // 첫화보기 로직은 최초 한번만 아이템을 적용하도록 대응
                val _episodeList = episodeList
                if (firstItem == null && _episodeList is Result.Success) {
                    firstItem = _episodeList.data.firstOrNull()
                }

                EpisodeScreen(
                    webToonItem = webToonItem,
                    isFavorite = isFavorite,
                    palletColor = palletColor,
                    episodeList = episodeList,
                    readIdSet = readIdSet,
                    firstItem = firstItem
                ) { action ->
                    handleAction(action)
                }
            }
        }

        viewModel.load()
    }

    private fun handleAction(action: EpisodeUiEvent) {
        when (action) {
            is EpisodeUiEvent.OnShowDetail -> {
                validItem(action.item) {
                    moveDetailPage(action.item)
                }
            }
            is EpisodeUiEvent.OnShowFirst -> {
                validItem(action.item) {
                    requestFirst()
                }
            }
            EpisodeUiEvent.OnBackPressed -> {
                finish()
            }
            is EpisodeUiEvent.UpdateFavorite -> {
                viewModel.favorite(action.isFavorite)
            }
            EpisodeUiEvent.MoreLoad -> {
                viewModel.load()
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
                    launcher = openDetailLauncher,
                    item = item,
                    palletColor = palletColor
                )
            }
        }
    }

    private fun updateFavorite(isFavorite: Boolean) {
        toast(if (isFavorite) "즐겨찾기 추가" else "즐겨찾기 제거")
    }

    private fun requestFirst() {
        viewModel.requestFirst()
    }

    private fun validItem(
        item: EpisodeInfo,
        successValidAction: (EpisodeInfo) -> Unit
    ) {
        if (item.isLock) {
            toast(com.pluu.webtoon.ui_common.R.string.msg_not_support)
        } else {
            successValidAction(item)
        }
    }
}
