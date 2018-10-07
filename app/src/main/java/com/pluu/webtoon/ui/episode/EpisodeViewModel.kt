package com.pluu.webtoon.ui.episode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.model.REpisode
import com.pluu.webtoon.usecase.EpisodeListUseCase
import com.pluu.webtoon.usecase.FavoriteUseCase
import com.pluu.webtoon.utils.bgDispatchers
import com.pluu.webtoon.utils.uiDispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * Episode ViewModel
 */
class EpisodeViewModel(
    private val serviceApi: AbstractEpisodeApi,
    private val info: WebToonInfo,
    private val listUseCase: EpisodeListUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val jobs = arrayListOf<Job>()

    private var isNext = true

    private val _listEvent = MutableLiveData<List<Episode>>()
    val listEvent: LiveData<List<Episode>>
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

    init {
        _favorite.value = info.isFavorite
    }

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }

    fun initalize() {
        serviceApi.init()
    }

    fun load() {
        if (!isNext) return

        jobs += GlobalScope.launch(uiDispatchers) {
            _event.value = EpisodeEvent.START

            val episodePage = async(bgDispatchers) {
                serviceApi.parseEpisode(info)
            }.await()

            val list = async(bgDispatchers) {
                val readList = getReadList()
                episodePage.episodes?.apply {
                    applyReaded(readList)
                }.orEmpty()
            }.await()

            isNext = !episodePage.moreLink().isNullOrBlank()

            if (list.isNotEmpty()) {
                _listEvent.value = list
                _event.value = EpisodeEvent.LOADED
            } else {
                _event.value = EpisodeEvent.ERROR
            }
        }
    }

    private fun getReadList() = listUseCase.getEpisode(info.toonId)

    fun readUpdate() {
        jobs += GlobalScope.launch(uiDispatchers) {
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

    fun requestFirst(item: Episode) {
        val firstItem = serviceApi.getFirstEpisode(item) ?: return
        firstItem.title = this.info.title
        _event.value = EpisodeEvent.FIRST(firstItem)
    }

    fun favorite(isFavorite: Boolean) {
        if (isFavorite) {
            favoriteUseCase.addFavorite(info.toonId)
        } else {
            favoriteUseCase.removeFavorite(info.toonId)
        }
        info.isFavorite = isFavorite

        _favorite.value = isFavorite
        _event.value = EpisodeEvent.UPDATE_FAVORITE(info.toonId, info.isFavorite)
    }
}

private fun List<Episode>.applyReaded(readList: List<REpisode>) {
    for (readItem in readList) {
        for (episode in this) {
            if (readItem.episodeId == episode.episodeId) {
                episode.setReadFlag()
                break
            }
        }
    }
}

sealed class EpisodeEvent {
    object START : EpisodeEvent()
    object LOADED : EpisodeEvent()
    class FIRST(val firstEpisode: Episode) : EpisodeEvent()
    object ERROR : EpisodeEvent()
    class UPDATE_FAVORITE(val id: String, val isFavorite: Boolean) : EpisodeEvent()
}
