package com.pluu.webtoon.ui.episode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.model.REpisode
import com.pluu.webtoon.usecase.AddFavoriteUseCase
import com.pluu.webtoon.usecase.EpisodeUseCase
import com.pluu.webtoon.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.usecase.RemoveFavoriteUseCase
import com.pluu.webtoon.utils.bgDispatchers
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.uiDispatchers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * EpisodeInfo ViewModel
 */
class EpisodeViewModel(
    private val info: ToonInfo,
    private val episodeUseCase: EpisodeUseCase,
    private val readEpisodeListUseCase: ReadEpisodeListUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val delFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val job by lazyNone { Job() }

    private var isNext = true

    private val _listEvent = MutableLiveData<List<EpisodeInfo>>()
    val listEvent: LiveData<List<EpisodeInfo>>
        get() = _listEvent

    private val _event = MutableLiveData<EpisodeEvent>()
    val event: LiveData<EpisodeEvent>
        get() = _event

    private val _updateListEvent = MutableLiveData<List<String>>()
    val updateListEvent: LiveData<List<String>>
        get() = _updateListEvent

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean>
        get() = _favorite

    private val INIT_PAGE = 0
    private var pageNo = INIT_PAGE
    private var firstEpisode: EpisodeInfo? = null

    init {
        _favorite.value = info.isFavorite
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    fun initalize() {
        pageNo = INIT_PAGE
    }

    fun load() {
        if (!isNext) return

        GlobalScope.launch(uiDispatchers) {
            _event.value = EpisodeEvent.START

            val episodePage = async(bgDispatchers) {
                episodeUseCase(EpisodeRequest(info.id, pageNo))
            }.await()
            when (episodePage) {
                is Result.Success -> {
                    val data = episodePage.data
                    val list = async(bgDispatchers) {
                        val result = data.episodes
                        val readList = getReadList()
                        result.applyReaded(readList)
                        result
                    }.await()

                    isNext = !data.nextLink.isNullOrBlank()

                    if (pageNo == INIT_PAGE) {
                        firstEpisode = data.first
                    }

                    if (list.isNotEmpty()) {
                        _listEvent.value = list
                    }
                    _event.value = EpisodeEvent.LOADED
                }
                is Result.Error -> {
                    _event.value = EpisodeEvent.ERROR
                }
            }
        }
    }

    private fun getReadList() = readEpisodeListUseCase(info.id)

    fun readUpdate() {
        GlobalScope.launch(uiDispatchers) {
            _event.value = EpisodeEvent.START

            val readList = async {
                getReadList().asSequence()
                    .mapNotNull { it.episodeId }
                    .distinct()
                    .toList()
            }.await()

            _updateListEvent.value = readList
            _event.value = EpisodeEvent.LOADED
        }
    }

    fun requestFirst() {
        firstEpisode?.let {
            _event.value = EpisodeEvent.FIRST(it)
        }
    }

    fun favorite(isFavorite: Boolean) {
        if (isFavorite) {
            addFavoriteUseCase(info.id)
        } else {
            delFavoriteUseCase(info.id)
        }
        info.isFavorite = isFavorite

        _favorite.value = isFavorite
        _event.value = EpisodeEvent.UPDATE_FAVORITE(info.id, info.isFavorite)
    }
}

private fun List<EpisodeInfo>.applyReaded(readList: List<REpisode>) {
    for (readItem in readList) {
        for (episode in this) {
            if (readItem.episodeId == episode.id) {
                episode.isRead = true
                break
            }
        }
    }
}

sealed class EpisodeEvent {
    object START : EpisodeEvent()
    object LOADED : EpisodeEvent()
    class FIRST(val firstEpisode: EpisodeInfo) : EpisodeEvent()
    object ERROR : EpisodeEvent()
    class UPDATE_FAVORITE(val id: String, val isFavorite: Boolean) : EpisodeEvent()
}
