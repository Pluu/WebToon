package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pluu.compose.ui.toast
import com.pluu.webtoon.episode.compose.ThemeCircularProgressIndicator
import com.pluu.webtoon.episode.compose.itemsInGrid
import com.pluu.webtoon.episode.ui.EpisodeEvent
import com.pluu.webtoon.episode.ui.EpisodeViewModel
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.ui_common.R

@Composable
internal fun EpisodeUi(
    viewModel: EpisodeViewModel,
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    eventAction: (EpisodeUiEvent) -> Unit,
    navigationAction: (EpisodeInfo) -> Unit,
    savedAction: (EpisodeEvent.UPDATE_FAVORITE) -> Unit
) {
    val event by viewModel.event.observeAsState(null)

    val readIdSet by viewModel.readIdSet.collectAsState()

    val episodeList = viewModel.episodePage.collectAsLazyPagingItems()

    val isFavorite by viewModel.favorite.observeAsState(false)

    when (val _event = event) {
        is EpisodeEvent.FIRST -> {
            navigationAction(_event.firstEpisode)
        }
        is EpisodeEvent.UPDATE_FAVORITE -> {
            toast(if (_event.isFavorite) "즐겨찾기 추가" else "즐겨찾기 제거")
            savedAction(_event)
        }
        else -> {}
    }

    if (episodeList.loadState.refresh is LoadState.Error) {
        toast(stringResource(R.string.network_fail))
        eventAction(EpisodeUiEvent.OnBackPressed)
        return
    }

    EpisodeUi(
        webToonItem,
        palletColor,
        episodeList,
        episodeList.itemSnapshotList.isNotEmpty(),
        isFavorite,
        readIdSet,
        updateFavoriteAction = { value ->
            viewModel.favorite(value)
        },
        eventAction
    )
}

@Composable
private fun EpisodeUi(
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    episodeList: LazyPagingItems<EpisodeInfo>,
    isFirstLoded: Boolean,
    isFavorite: Boolean,
    readIdSet: Set<EpisodeId>,
    updateFavoriteAction: (Boolean) -> Unit,
    eventAction: (EpisodeUiEvent) -> Unit
) {
    EpisodeScreen(
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        palletColor = palletColor,
        isFirstLoded = isFirstLoded,
        updateFavoriteAction = updateFavoriteAction,
        eventAction = eventAction
    ) { innerPadding ->
        EpisodeLoadingUi(
            modifier = Modifier.padding(innerPadding),
            episodeList = episodeList
        ) {
            EpisodeGridContent(
                items = episodeList,
                readIdSet = readIdSet,
                onEpisodeClicked = { item -> eventAction(EpisodeUiEvent.OnShowDetail(item)) }
            )
        }
    }
}

@Composable
private fun EpisodeGridContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<EpisodeInfo>,
    readIdSet: Set<EpisodeId>,
    onEpisodeClicked: (EpisodeInfo) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsInGrid(
            items = items,
            columns = 2
        ) { item ->
            EpisodeItemUi(
                item = item,
                isRead = readIdSet.contains(item.id),
                onClicked = onEpisodeClicked
            )
        }

        // 추가 로딩시에 노출하는 Footer Loading
        if (items.loadState.append == LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ThemeCircularProgressIndicator(
                        modifier = Modifier.padding(12.dp),
                        circleSize = 32.dp
                    )
                }
            }
        }
    }
}