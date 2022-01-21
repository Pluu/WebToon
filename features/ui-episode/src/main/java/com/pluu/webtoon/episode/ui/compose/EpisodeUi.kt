package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pluu.compose.foundation.InfiniteListHandler
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.ProgressDialog
import com.pluu.compose.ui.toast
import com.pluu.ui.state.UiState
import com.pluu.utils.toUiState
import com.pluu.webtoon.episode.ui.EpisodeEvent
import com.pluu.webtoon.episode.ui.EpisodeViewModel
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.model.successOr
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.ui_common.R

@Composable
fun EpisodeUi(
    viewModel: EpisodeViewModel,
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    eventAction: (EpisodeUiEvent) -> Unit,
    navigationAction: (EpisodeInfo) -> Unit,
    savedAction: (EpisodeEvent.UPDATE_FAVORITE) -> Unit
) {
    var showDialog by rememberMutableStateOf(false)

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
            navigationAction(_event.firstEpisode)
        }
        is EpisodeEvent.UPDATE_FAVORITE -> {
            toast(if (_event.isFavorite) "즐겨찾기 추가" else "즐겨찾기 제거")
            savedAction(_event)
        }
        else -> {}
    }

    if (episodeList is Result.Error) {
        toast(stringResource(R.string.network_fail))
        eventAction(EpisodeUiEvent.OnBackPressed)
        return
    }

    if (showDialog) {
        ProgressDialog(title = "Loading...")
    }

    var isFirstLoded by rememberMutableStateOf(false)
    if (!isFirstLoded) {
        isFirstLoded = episodeList.successOr(emptyList()).isNotEmpty()
    }

    EpisodeUi(
        webToonItem,
        palletColor,
        episodeList,
        isFirstLoded,
        isFavorite,
        readIdSet,
        eventAction
    )
}

@Composable
private fun EpisodeUi(
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    episodeList: Result<List<EpisodeInfo>>,
    isFirstLoded: Boolean,
    isFavorite: Boolean,
    readIdSet: Set<EpisodeId>,
    eventAction: (EpisodeUiEvent) -> Unit
) {
    EpisodeScreen(
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        palletColor = palletColor,
        isFirstLoded = isFirstLoded,
        eventAction = eventAction
    ) { innerPadding ->
        EpisodeContentUi(
            modifier = Modifier.padding(innerPadding),
            uiState = episodeList.toUiState { list ->
                UiState(
                    data = list.takeIf { it.isNotEmpty() },
                    loading = list.isEmpty()
                )
            }
        ) { items ->
            EpisodeGridContent(
                items = items,
                readIdSet = readIdSet,
                onMoreLoaded = { eventAction(EpisodeUiEvent.MoreLoad) },
                onEpisodeClicked = { item -> eventAction(EpisodeUiEvent.OnShowDetail(item)) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EpisodeGridContent(
    modifier: Modifier = Modifier,
    items: List<EpisodeInfo>,
    onMoreLoaded: () -> Unit,
    readIdSet: Set<EpisodeId>,
    onEpisodeClicked: (EpisodeInfo) -> Unit
) {
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        cells = GridCells.Fixed(2),
        state = listState
    ) {
        items(items) { item ->
            EpisodeItemUi(
                item = item,
                isRead = readIdSet.contains(item.id),
                onClicked = onEpisodeClicked
            )
        }
    }

    InfiniteListHandler(
        listState = listState,
        buffer = 3
    ) {
        onMoreLoaded()
    }
}