package com.pluu.webtoon.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.webtoon.data.network.DetailRequest
import com.pluu.webtoon.domain.moel.*
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.ReadUseCase
import com.pluu.webtoon.domain.usecase.ShareUseCase
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val dispatchers: AppCoroutineDispatchers,
    private val episode: EpisodeInfo,
    private val detailUseCase: DetailUseCase,
    private val readUseCase: ReadUseCase,
    private val shareUseCase: ShareUseCase
) : ViewModel() {

    private val _event = MutableLiveData<DetailEvent>()
    val event: LiveData<DetailEvent>
        get() = _event

    private val _elementEvent = MutableLiveData<ElementEvent>()
    val elementEvent: LiveData<ElementEvent>
        get() = _elementEvent

    private val _list = MutableLiveData<List<DetailView>>()
    val list: LiveData<List<DetailView>>
        get() = _list

    private var currentItem: DetailResult.Detail? = null

    init {
        loadDetail(episode)
    }

    fun movePrev() {
        // TODO: Prev EpisodeInfo
    }

    fun moveNext() {
        // TODO: Next EpisodeInfo
    }

    private fun loadDetail(episode: EpisodeInfo) {
        _event.value = DetailEvent.START

        viewModelScope.launch {
            var error: DetailEvent? = null

            when (val result: DetailResult = readDetail(episode)) {
                is DetailResult.Detail -> {
                    updateEpisodeState(result)

                    currentItem = result
                    _list.value = result.list.filter {
                        it.url.isNotEmpty()
                    }
                    _elementEvent.value = ElementEvent(
                        title = result.title.orEmpty(),
                        webToonTitle = episode.title,
                        isPrevEnable = result.prevLink?.isNotEmpty() == true,
                        isNextEnable = result.nextLink?.isNotEmpty() == true
                    )
                }
                is DetailResult.ErrorResult -> {
                    error = DetailEvent.ERROR(result.errorType)
                }
            }
            _event.value = error ?: DetailEvent.LOADED
        }
    }

    private suspend fun readDetail(
        episode: EpisodeInfo
    ): DetailResult = withContext(dispatchers.computation) {
        runCatching {
            detailUseCase(
                DetailRequest(
                    toonId = episode.toonId,
                    episodeId = episode.id,
                    episodeTitle = episode.title
                )
            )
        }.getOrElse {
            DetailResult.ErrorResult(errorType = ERROR_TYPE.DEFAULT_ERROR)
        }
    }

    private suspend fun updateEpisodeState(
        item: DetailResult.Detail
    ) = withContext(dispatchers.computation) {
        readUseCase(item)
    }

    fun requestShare() {
        val item = currentItem ?: return
        _event.value = DetailEvent.SHARE(
            shareUseCase(
                episode = episode,
                detail = item
            )
        )
    }
}

sealed class DetailEvent {
    object START : DetailEvent()
    object LOADED : DetailEvent()
    class ERROR(
        val errorType: ERROR_TYPE
    ) : DetailEvent()

    class SHARE(val item: ShareItem) : DetailEvent()
}

class ElementEvent(
    val title: String,
    val webToonTitle: String,
    val isPrevEnable: Boolean,
    val isNextEnable: Boolean
)
