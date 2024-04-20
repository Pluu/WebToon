package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.pluu.compose.ui.toast
import com.pluu.utils.toast
import com.pluu.webtoon.episode.compose.ThemeCircularProgressIndicator
import com.pluu.webtoon.episode.ui.EpisodeEvent
import com.pluu.webtoon.episode.ui.EpisodeScreen
import com.pluu.webtoon.episode.ui.EpisodeUiEvent
import com.pluu.webtoon.episode.ui.EpisodeViewModel
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.ui_common.R

@Composable
fun EpisodeUi(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    openDetail: (EpisodeInfo) -> Unit,
    closeCurrent: () -> Unit
) {
    EpisodeUi(
        modifier = modifier.fillMaxSize(),
        viewModel = hiltViewModel(),
        webToonItem = webToonItem,
        palletColor = palletColor,
        openDetail = openDetail,
        closeCurrent = closeCurrent
    )
}

@Composable
private fun EpisodeUi(
    modifier: Modifier = Modifier,
    viewModel: EpisodeViewModel,
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    openDetail: (EpisodeInfo) -> Unit,
    closeCurrent: () -> Unit
) {
    val context = LocalContext.current

    val readIdSet by viewModel.readIdSet.collectAsStateWithLifecycle()

    val episodeList = viewModel.episodePage.collectAsLazyPagingItems()

    val isFavorite by viewModel.favorite.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is EpisodeEvent.UPDATE_FAVORITE -> {
                    context.toast(if (event.isFavorite) "즐겨찾기 추가" else "즐겨찾기 제거")
                }
            }
        }
    }

    if (episodeList.loadState.append is LoadState.Error) {
        toast(stringResource(R.string.network_fail))
        return
    }

    EpisodeUi(
        modifier = modifier,
        webToonItem = webToonItem,
        palletColor = palletColor,
        episodeList = episodeList,
        isFirstLoaded = episodeList.itemSnapshotList.isNotEmpty(),
        isFavorite = isFavorite,
        readIdSet = readIdSet,
        updateFavoriteAction = { value ->
            viewModel.favorite(value)
        },
        eventAction = { action ->
            when (action) {
                EpisodeUiEvent.OnBackPressed -> {
                    closeCurrent()
                }

                is EpisodeUiEvent.OnShowDetail -> {
                    openDetail(action.item)
                }

                EpisodeUiEvent.OnShowFirst -> {
                    val firstInfo = viewModel.requestFirst()
                    if (firstInfo == null) {
                        context.toast(R.string.unknown_fail)
                    } else {
                        openDetail(firstInfo)
                    }
                }
            }
        }
    )
}

@Composable
private fun EpisodeUi(
    modifier: Modifier = Modifier,
    webToonItem: ToonInfoWithFavorite,
    palletColor: PalletColor,
    episodeList: LazyPagingItems<EpisodeInfo>,
    isFirstLoaded: Boolean,
    isFavorite: Boolean,
    readIdSet: Set<EpisodeId>,
    updateFavoriteAction: (Boolean) -> Unit,
    eventAction: (EpisodeUiEvent) -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    EpisodeScreen(
        modifier = modifier,
        webToonItem = webToonItem,
        isFavorite = isFavorite,
        palletColor = palletColor,
        isFirstLoaded = isFirstLoaded,
        updateFavoriteAction = updateFavoriteAction,
        eventAction = eventAction
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (episodeList.loadState.refresh == LoadState.Loading) {
                EpisodeLoadingUi()
            }
            EpisodeGridContent(
                items = episodeList,
                lazyGridState = lazyGridState,
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
    lazyGridState: LazyGridState = rememberLazyGridState(),
    readIdSet: Set<EpisodeId> = emptySet(),
    onEpisodeClicked: (EpisodeInfo) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        state = lazyGridState,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id }
        ) { index ->
            EpisodeItemUi(
                item = items[index]!!,
                isRead = readIdSet.contains(items[index]!!.id),
                onClicked = onEpisodeClicked
            )
        }

        // 추가 로딩시에 노출하는 Footer Loading
        if (items.loadState.append == LoadState.Loading) {
            item {
                ThemeCircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .wrapContentSize(),
                    circleSize = 32.dp
                )
            }
        }
    }
}