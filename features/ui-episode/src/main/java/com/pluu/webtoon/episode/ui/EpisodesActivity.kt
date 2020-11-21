package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.pluu.compose.transition.colorStartToEndTransition
import com.pluu.compose.transition.colorStateKey
import com.pluu.compose.transition.colorTransitionDefinition
import com.pluu.compose.ui.ProgressDialog
import com.pluu.compose.ui.graphics.toColor
import com.pluu.compose.utils.ProvideDisplayInsets
import com.pluu.core.utils.lazyNone
import com.pluu.utils.ThemeHelper
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.utils.result.registerStartActivityForResult
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.ui.state.UiState
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.compose.ActivityComposeView
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class EpisodesActivity : AppCompatActivity() {

    private val viewModel by viewModels<EpisodeViewModel>()

    private val webToonItem by lazyNone {
        intent.getRequiredSerializableExtra<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)
    }
    private val palletColor by lazyNone {
        intent.getRequiredSerializableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    @Inject
    lateinit var detailNavigator: DetailNavigator

    private val openDetailLauncher = registerStartActivityForResult {
        viewModel.readUpdate()
    }

    private val colorDefinition by lazy {
        colorTransitionDefinition(
            startColor = getThemeColor(R.attr.colorPrimary).toColor(),
            endColor = palletColor.darkVibrantColor.toColor(),
            durationMillis = 1000
        )
    }

    private val infoTextColorDefinition by lazy {
        colorTransitionDefinition(
            startColor = getThemeColor(android.R.attr.textColorPrimary).toColor(),
            endColor = if (ThemeHelper.isLightTheme(this)) {
                palletColor.darkMutedColor.toColor()
            } else {
                palletColor.lightMutedColor.toColor()
            },
            durationMillis = 1000
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ActivityComposeView {
            ProvideDisplayInsets {
                var showDialog by remember { mutableStateOf(false) }
                var isRefresh by remember { mutableStateOf(false) }

                val episodeList by viewModel.listEvent.observeAsState(
                    Result.Success(emptyList())
                )
                val event by viewModel.event.observeAsState(null)

                val readIdSet by viewModel.readIdSet.observeAsState(emptySet())

                if (showDialog) {
                    ProgressDialog("Loading...")
                }

                val _event = event
                if (_event != null) {
                    when (_event) {
                        EpisodeEvent.START -> {
                            showDialog = true
                        }
                        EpisodeEvent.LOADED -> {
                            showDialog = false
                            isRefresh = false
                        }
                        is EpisodeEvent.FIRST -> {
                            moveDetailPage(_event.firstEpisode)
                        }
                        is EpisodeEvent.UPDATE_FAVORITE -> {
                            updateFavorite(_event.isFavorite)
                            savedResult(_event)
                        }
                    }
                }

                if (episodeList is Result.Error) {
                    toast(R.string.network_fail)
                    finish()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { initEpisodeTopUi() },
                    bottomBar = {
                        val _episodeList = episodeList
                        if (_episodeList is Result.Success) {
                            // TODO: 첫화 보기
                            val firstPendingItem = _episodeList.data?.firstOrNull()
                            if (firstPendingItem != null) {
                                initEpisodeInfoUi(firstPendingItem) { item ->
                                    validItem(item) {
                                        requestFirst()
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    EpisodeContentUi(
                        uiState = convertUiState(episodeList),
                        readIdSet = readIdSet,
                        modifier = Modifier.padding(innerPadding),
                        onMoreLoaded = {
                            if (showDialog) return@EpisodeContentUi
                            viewModel.load()
                        },
                        onRefresh = {
                            viewModel.initialize()
                            viewModel.load()
                        }
                    ) { item ->
                        validItem(item) {
                            moveDetailPage(item)
                        }
                    }
                }
            }
        }

        viewModel.load()
    }

    private fun convertUiState(value: Result<List<EpisodeInfo>>): UiState<List<EpisodeInfo>> {
        return when (value) {
            is Result.Success -> UiState(
                data = value.data.takeIf { it.isNotEmpty() },
                loading = value.data.isEmpty()
            )
            is Result.Error -> UiState(exception = value.exception)
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

    @Composable
    private fun initEpisodeTopUi() {
        val isFavorite by viewModel.favorite.observeAsState(false)
        val transition = colorStartToEndTransition(colorDefinition)

        EpisodeTopUi(
            title = webToonItem.info.title,
            backgroundColor = transition[colorStateKey],
            onBackPressed = {
                finish()
            },
            isFavorite = isFavorite,
            onFavoriteClicked = { currentFavorite ->
                viewModel.favorite(currentFavorite.not())
            }
        )
    }

    @Composable
    private fun initEpisodeInfoUi(
        firstItem: EpisodeInfo,
        onFirstClicked: (EpisodeInfo) -> Unit
    ) {
        val info = webToonItem.info
        val bgTransition = colorStartToEndTransition(colorDefinition)
        val infoTextTransition = colorStartToEndTransition(infoTextColorDefinition)

        EpisodeInfoUi(
            name = info.writer,
            rate = info.rate,
            infoTextColor = infoTextTransition[colorStateKey],
            buttonBackgroundColor = bgTransition[colorStateKey],
            onFirstClicked = {
                onFirstClicked(firstItem)
            }
        )
    }

    private fun validItem(
        item: EpisodeInfo,
        successValidAction: (EpisodeInfo) -> Unit
    ) {
        if (item.isLock) {
            toast(R.string.msg_not_support)
        } else {
            successValidAction(item)
        }
    }

    private fun moveDetailPage(item: EpisodeInfo) {
        detailNavigator.openDetail(
            context = this,
            launcher = openDetailLauncher,
            item = item,
            palletColor = palletColor
        )
    }

    private fun updateFavorite(isFavorite: Boolean) {
        toast(if (isFavorite) "즐겨찾기 추가" else "즐겨찾기 제거")
    }

    private fun requestFirst() {
        viewModel.requestFirst()
    }
}
