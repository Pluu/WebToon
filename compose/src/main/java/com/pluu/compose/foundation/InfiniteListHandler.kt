package com.pluu.compose.foundation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

///////////////////////////////////////////////////////////////////////////
// Origin : https://dev.to/luismierez/infinite-lazycolumn-in-jetpack-compose-44a4
///////////////////////////////////////////////////////////////////////////

/**
 * Handler to make any lazy column (or lazy row) infinite. Will notify the [onLoadMore]
 * callback once needed
 * @param listState state of the list that needs to also be passed to the LazyColumn composable.
 * Get it by calling rememberLazyListState()
 * @param buffer the number of items before the end of the list to call the onLoadMore callback
 * @param onLoadMore will notify when we need to load more
 */
@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    includeEmpty: Boolean = false,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            if (!includeEmpty && totalItemsNumber == 0) return@derivedStateOf false
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadMore()
            }
    }
}