package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.pluu.compose.runtime.rememberMutableStateOf
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
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
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

                InitContentUi(
                    webToonItem,
                    isFavorite,
                    palletColor,
                    episodeList,
                    readIdSet,
                    firstItem
                ) { action ->
                    handleAction(action)
                }
            }
        }

        viewModel.load()
    }

    private fun handleAction(action: EpisodeAction) {
        when (action) {
            is EpisodeAction.OnShowDetail -> {
                validItem(action.item) {
                    moveDetailPage(action.item)
                }
            }
            is EpisodeAction.OnShowFirst -> {
                validItem(action.item) {
                    requestFirst()
                }
            }
            EpisodeAction.OnBackPressed -> {
                finish()
            }
            is EpisodeAction.UpdateFavorite -> {
                viewModel.favorite(action.isFavorite)
            }
            EpisodeAction.MoreLoad -> {
                viewModel.load()
            }
            EpisodeAction.Refresh -> {
                viewModel.initialize()
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
}

sealed class EpisodeAction {
    class OnShowFirst(val item: EpisodeInfo) : EpisodeAction()
    class OnShowDetail(val item: EpisodeInfo) : EpisodeAction()
    object OnBackPressed : EpisodeAction()
    class UpdateFavorite(val isFavorite: Boolean) : EpisodeAction()
    object Refresh : EpisodeAction()
    object MoreLoad : EpisodeAction()
}

@Composable
private fun InitContentUi(
    webToonItem: ToonInfoWithFavorite,
    isFavorite: Boolean,
    palletColor: PalletColor,
    episodeList: Result<List<EpisodeInfo>>,
    readIdSet: Set<EpisodeId>,
    firstItem: EpisodeInfo?,
    eventAction: (EpisodeAction) -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(ColorTransitionState.START).apply {
            targetState = ColorTransitionState.END
        }
    }

    val transition = updateTransition(transitionState)
    val featureBgColor by animateBgColor(palletColor, transition)
    val featureTextColor by animateTestColor(palletColor, transition)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EpisodeTopUi(
                title = webToonItem.info.title,
                isFavorite = isFavorite,
                backgroundColor = featureBgColor,
                onBackPressed = { eventAction(EpisodeAction.OnBackPressed) }
            ) { currentFavorite ->
                eventAction(EpisodeAction.UpdateFavorite(currentFavorite.not()))
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(62.dp)
                    .padding(3.dp)
            ) {
                if (firstItem != null) {
                    EpisodeInfoUi(
                        name = webToonItem.info.writer,
                        rate = webToonItem.info.rate,
                        infoTextColor = featureTextColor,
                        buttonBackgroundColor = featureBgColor,
                        onFirstClicked = {
                            eventAction(EpisodeAction.OnShowFirst(firstItem))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        EpisodeContentUi(
            uiState = convertUiState(episodeList),
            readIdSet = readIdSet,
            modifier = Modifier.padding(innerPadding),
            onMoreLoaded = { eventAction(EpisodeAction.MoreLoad) },
            onRefresh = { eventAction(EpisodeAction.Refresh) }
        ) { item ->
            eventAction(EpisodeAction.OnShowDetail(item))
        }
    }
}

private fun convertUiState(value: Result<List<EpisodeInfo>>): UiState<List<EpisodeInfo>> {
    return when (value) {
        is Result.Success -> UiState(
            data = value.data.takeIf { it.isNotEmpty() },
            loading = value.data.isEmpty()
        )
        is Result.Error -> UiState(exception = value.exception)
        else -> UiState()
    }
}

@Composable
private fun animateBgColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    val context = LocalContext.current
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) }
    ) { state ->
        when (state) {
            ColorTransitionState.START -> context.getThemeColor(R.attr.colorPrimary).toColor()
            ColorTransitionState.END -> palletColor.darkVibrantColor.toColor()
        }
    }
}

@Composable
private fun animateTestColor(
    palletColor: PalletColor,
    transition: Transition<ColorTransitionState>
): State<Color> {
    val context = LocalContext.current
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) }
    ) { state ->
        when (state) {
            ColorTransitionState.START -> context.getThemeColor(android.R.attr.textColorPrimary)
                .toColor()
            ColorTransitionState.END -> if (ThemeHelper.isLightTheme(context)) {
                palletColor.darkMutedColor.toColor()
            } else {
                palletColor.lightMutedColor.toColor()
            }
        }
    }
}
