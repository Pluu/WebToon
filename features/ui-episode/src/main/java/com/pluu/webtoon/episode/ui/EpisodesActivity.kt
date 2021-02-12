package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.pluu.compose.transition.ColorTransitionState
import com.pluu.compose.ui.ProgressDialog
import com.pluu.compose.ui.graphics.toColor
import com.pluu.core.utils.lazyNone
import com.pluu.ui.state.UiState
import com.pluu.utils.ThemeHelper
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.utils.result.registerStartActivityForResult
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.compose.ActivityComposeView
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ActivityComposeView {
            ProvideWindowInsets {
                var showDialog by remember { mutableStateOf(false) }

                var firstItem by remember { mutableStateOf<EpisodeInfo?>(null) }

                val episodeList by viewModel.listEvent.observeAsState(
                    Result.Success(emptyList())
                )
                val event by viewModel.event.observeAsState(null)

                val readIdSet by viewModel.readIdSet.observeAsState(emptySet())

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
                }

                if (episodeList is Result.Error) {
                    toast(R.string.network_fail)
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

                InitContentUi(episodeList, readIdSet, firstItem)
            }
        }

        viewModel.load()
    }

    @Composable
    private fun InitContentUi(
        episodeList: Result<List<EpisodeInfo>>,
        readIdSet: Set<EpisodeId>,
        firstItem: EpisodeInfo?
    ) {
        var transitionState by remember { mutableStateOf(ColorTransitionState.START) }

        val transition = updateTransition(transitionState)

        val featureColor: Color by transition.animateColor(
            transitionSpec = { tween(durationMillis = 1000) }
        ) { state ->
            when (state) {
                ColorTransitionState.START -> getThemeColor(R.attr.colorPrimary).toColor()
                ColorTransitionState.END -> palletColor.darkVibrantColor.toColor()
            }
        }

        val featureTextColor: Color by transition.animateColor(
            transitionSpec = { tween(durationMillis = 1000) }
        ) { state ->
            when (state) {
                ColorTransitionState.START -> getThemeColor(android.R.attr.textColorPrimary).toColor()
                ColorTransitionState.END -> if (ThemeHelper.isLightTheme(this)) {
                    palletColor.darkMutedColor.toColor()
                } else {
                    palletColor.lightMutedColor.toColor()
                }
            }
        }

        SideEffect {
            transitionState = ColorTransitionState.END
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { InitEpisodeTopUi(featureColor) },
            bottomBar = {
                if (firstItem != null) {
                    InitEpisodeInfoUi(firstItem, featureColor, featureTextColor) { item ->
                        validItem(item) {
                            requestFirst()
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

    @Composable
    private fun InitEpisodeTopUi(
        featureColor: Color
    ) {
        val isFavorite by viewModel.favorite.observeAsState(false)

        EpisodeTopUi(
            title = webToonItem.info.title,
            backgroundColor = featureColor,
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
    private fun InitEpisodeInfoUi(
        firstItem: EpisodeInfo,
        featureColor: Color,
        textFeatureColor: Color,
        onFirstClicked: (EpisodeInfo) -> Unit
    ) {
        val info = webToonItem.info

        EpisodeInfoUi(
            name = info.writer,
            rate = info.rate,
            infoTextColor = textFeatureColor,
            buttonBackgroundColor = featureColor,
            onFirstClicked = {
                onFirstClicked(firstItem)
            }
        )
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
