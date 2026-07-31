package com.pluu.webtoon.detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.ui.state.UiState
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.domain.usecase.ReadUseCase
import com.pluu.webtoon.domain.usecase.site.GetDetailUseCase
import com.pluu.webtoon.domain.usecase.site.GetShareUseCase
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.getLogMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
internal class DetailViewModel @AssistedInject constructor(
    private val type: NAV_ITEM,
    @Assisted private val episode: EpisodeInfo,
    private val dispatchers: AppCoroutineDispatchers,
    private val getDetailUseCase: GetDetailUseCase,
    private val readUseCase: ReadUseCase,
    private val getShareUseCase: GetShareUseCase
) : ViewModel() {
    val event: LiveData<DetailEvent>
        field = MutableLiveData<DetailEvent>()

    val elementUiState: LiveData<UiState<ElementEvent>>
        field = MutableLiveData<UiState<ElementEvent>>()

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
        event.value = DetailEvent.START

        viewModelScope.launch {
            var error: DetailEvent? = null

            elementUiState.value = UiState(loading = true)

            when (val result: DetailResult = readDetail(episode)) {
                is DetailResult.Detail -> {
                    updateEpisodeState(result)

                    currentItem = result

                    element = ElementEvent(
                        title = result.title,
                        webToonTitle = episode.toonTitle,
                        prevEpisodeId = result.prevLink,
                        nextEpisodeId = result.nextLink,
                        list = result.list.filter {
                            it.url.isNotEmpty()
                        }
                    )

                    elementUiState.value = UiState(data = element)
                }

                is DetailResult.ErrorResult -> {
                    error = DetailEvent.ERROR(result.errorType)
                    Timber.e(result.errorType.getLogMessage())
                }
            }
            event.value = error ?: DetailEvent.LOADED
        }
    }

    private suspend fun readDetail(
        episode: EpisodeInfo
    ): DetailResult = withContext(dispatchers.computation) {
        runCatching {
            getDetailUseCase(
                toonId = episode.toonId,
                episodeId = episode.id,
                episodeTitle = episode.title
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
        event.value = DetailEvent.SHARE(
            getShareUseCase(
                toonId = episode.toonId,
                episodeId = episode.id,
                episodeTitle = episode.title,
                detailTitle = item.title
            )
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(episode: EpisodeInfo): DetailViewModel
    }
}

internal sealed class DetailEvent {
    data object START : DetailEvent()
    data object LOADED : DetailEvent()
    class ERROR(val errorType: ERROR_TYPE) : DetailEvent()
    class SHARE(val item: ShareItem) : DetailEvent()
}

internal class ElementEvent(
    val title: String,
    val webToonTitle: String,
    val prevEpisodeId: String?,
    val nextEpisodeId: String?,
    val list: List<DetailView>
)
