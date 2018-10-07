package com.pluu.webtoon.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.ERROR_TYPE
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.usecase.ReadUseCase
import com.pluu.webtoon.utils.withUIDispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch

class DetailViewModel(
    private val serviceApi: AbstractDetailApi,
    episode: Episode,
    private val readUseCase: ReadUseCase
) : ViewModel() {

    private val jobs = arrayListOf<Job>()

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

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }

    fun movePrev() {
        // TODO: Prev Episode
    }

    fun moveNext() {
        // TODO: Next Episode
    }

    private fun loadDetail(episode: Episode) {
        _event.value = DetailEvent.START
        jobs += GlobalScope.launch {
            var error: DetailEvent? = null
            val result = try {
                serviceApi.parseDetail(episode)
            } catch (e: Exception) {
                e.printStackTrace()
                error = DetailEvent.ERROR(errorType = ERROR_TYPE.DEFAULT_ERROR)
                null
            }

            if (result is DetailResult.Detail) {
                readEpisode(result)
            }

            withUIDispatchers {
                when (result) {
                    is DetailResult.Detail -> {
                        currentItem = result
                        _list.value = result.list
                        _elementEvent.value = ElementEvent(
                            title = result.title.orEmpty(),
                            webToonTitle = episode.title.orEmpty(),
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
    }

    private fun readEpisode(item: DetailResult.Detail) {
        readUseCase.readEpisode(item)
    }
}

sealed class DetailEvent {
    object START : DetailEvent()
    object LOADED : DetailEvent()
    class ERROR(
        val errorType: ERROR_TYPE
    ) : DetailEvent()
}

class ElementEvent(
    val title: String,
    val webToonTitle: String,
    val isPrevEnable: Boolean,
    val isNextEnable: Boolean
)
