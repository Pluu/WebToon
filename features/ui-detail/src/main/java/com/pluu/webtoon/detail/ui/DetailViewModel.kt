package com.pluu.webtoon.detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.ui.state.UiState
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.ReadUseCase
import com.pluu.webtoon.domain.usecase.ShareUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.getLogMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val detailUseCase: DetailUseCase,
    private val readUseCase: ReadUseCase,
    private val shareUseCase: ShareUseCase
) : ViewModel() {

    private val episode = handle.get<EpisodeInfo>(Const.EXTRA_EPISODE)!!

    private val _event = MutableLiveData<DetailEvent>()
    val event: LiveData<DetailEvent> get() = _event

    private val _elementUiState = MutableLiveData<UiState<ElementEvent>>()
    val elementUiState: LiveData<UiState<ElementEvent>> get() = _elementUiState

    private lateinit var element: ElementEvent
    private var currentItem: DetailResult.Detail? = null

    init {
        loadDetail(episode)
    }

    fun movePrev() {
        element.prevEpisodeId?.let {
            loadDetail(episode.copy(id = it))
        }
    }

    fun moveNext() {
        element.nextEpisodeId?.let {
            loadDetail(episode.copy(id = it))
        }
    }

    private fun loadDetail(episode: EpisodeInfo) {
        _event.value = DetailEvent.START

        viewModelScope.launch {
            var error: DetailEvent? = null

            _elementUiState.value = UiState(loading = true)

            when (val result: DetailResult = readDetail(episode)) {
                is DetailResult.Detail -> {
                    updateEpisodeState(result)

                    currentItem = result

                    element = ElementEvent(
                        title = result.title.orEmpty(),
                        webToonTitle = episode.title,
                        prevEpisodeId = result.prevLink,
                        nextEpisodeId = result.nextLink,
                        list = result.list.filter {
                            it.url.isNotEmpty()
                        }
                    )

                    _elementUiState.value = UiState(data = element)
                }
                is DetailResult.ErrorResult -> {
                    error = DetailEvent.ERROR(result.errorType)
                    Timber.e(result.errorType.getLogMessage())
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
            DetailResult.ErrorResult(errorType = ERROR_TYPE.DEFAULT_ERROR(it))
        }
    }

    private suspend fun updateEpisodeState(
        item: DetailResult.Detail
    ) = withContext(dispatchers.computation) {
        readUseCase(type, item)
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
    class ERROR(val errorType: ERROR_TYPE) : DetailEvent()
    class SHARE(val item: ShareItem) : DetailEvent()
}

class ElementEvent(
    val title: String,
    val webToonTitle: String,
    val prevEpisodeId: String?,
    val nextEpisodeId: String?,
    val list: List<DetailView>
)
