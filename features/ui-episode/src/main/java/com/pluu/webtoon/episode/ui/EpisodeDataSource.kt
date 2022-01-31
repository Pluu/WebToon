package com.pluu.webtoon.episode.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pluu.webtoon.domain.usecase.site.GetEpisodeUseCase
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonId

class EpisodeDataSource(
    private val id: ToonId,
    private val getEpisodeUseCase: GetEpisodeUseCase
) : PagingSource<Int, EpisodeResult>() {
    private val INIT_PAGE = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeResult> {
        return try {
            val page = params.key ?: INIT_PAGE
            when (val result = getEpisodeUseCase(id, page)) {
                is Result.Error -> {
                    LoadResult.Error(result.throwable)
                }
                is Result.Success -> {
                    LoadResult.Page(
                        data = listOf(result.data),
                        prevKey = (page - 1).takeIf { it >= INIT_PAGE },
                        nextKey = (page + 1).takeIf { result.data.nextLink != null }
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodeResult>): Int? {
        return state.anchorPosition
    }
}