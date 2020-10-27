package com.pluu.webtoon.episode.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.usecase.AddFavoriteUseCase
import com.pluu.webtoon.domain.usecase.EpisodeUseCase
import com.pluu.webtoon.domain.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.domain.usecase.RemoveFavoriteUseCase
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfoWithFavorite
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** EpisodeInfo ViewModel */
class EpisodeViewModel @ViewModelInject constructor(
    @Assisted handle: SavedStateHandle,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val episodeUseCase: EpisodeUseCase,
    private val readEpisodeListUseCase: ReadEpisodeListUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val delFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val item = handle.get<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)!!
    private val id = item.info.id

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

    private val _favorite = MutableLiveData(item.isFavorite)
    val favorite: LiveData<Boolean>
        get() = _favorite

    private val INIT_PAGE = 0
    private var pageNo = INIT_PAGE
    private var firstEpisode: EpisodeInfo? = null

    fun initialize() {
        pageNo = INIT_PAGE
        isNext = true
    }

    fun load() {
        if (!isNext) return
        isNext = !isNext

        viewModelScope.launch {
            _event.value = EpisodeEvent.START

            when (val episodePage = getEpisodeUseCase(id, pageNo)) {
                is Result.Success -> {
                    val data = episodePage.data

                    actionSuccessUi(data)

                    val resultList = successProcess(data)
                    if (resultList.isNotEmpty()) {
                        _listEvent.value = resultList
                    }
                    _event.value = EpisodeEvent.LOADED
                }
                is Result.Error -> {
                    _event.value = EpisodeEvent.ERROR
                }
            }
        }
    }

    private suspend fun getEpisodeUseCase(
        id: String,
        page: Int
    ): Result<EpisodeResult> = withContext(dispatchers.computation) {
        episodeUseCase(EpisodeRequest(id, page))
    }

    private fun actionSuccessUi(
        data: EpisodeResult
    ) {
        isNext = !data.nextLink.isNullOrBlank()

        if (pageNo == INIT_PAGE) {
            firstEpisode = data.first
        }
        if (isNext) {
            pageNo += 1
        }
    }

    private suspend fun successProcess(
        episodePage: EpisodeResult
    ): List<EpisodeInfo> = withContext(dispatchers.computation) {
        runCatching {
            val result = episodePage.episodes
            val readList = getReadList()
            result.applyReaded(readList)
            result
        }.getOrDefault(emptyList())
    }

    private suspend fun getReadList(): List<Episode> = withContext(dispatchers.computation) {
        readEpisodeListUseCase(type, id)
    }

    fun readUpdate() {
        viewModelScope.launch {
            _event.value = EpisodeEvent.START
            _updateListEvent.value = readableEpisodeList()
            _event.value = EpisodeEvent.LOADED
        }
    }

    private suspend fun readableEpisodeList() = withContext(dispatchers.computation) {
        runCatching {
            getReadList().asSequence()
                .mapNotNull { it.episodeId }
                .distinct()
                .toList()
        }.getOrDefault(emptyList())
    }

    fun requestFirst() {
        firstEpisode?.let {
            _event.value = EpisodeEvent.FIRST(it)
        }
    }

    fun favorite(isFavorite: Boolean) {
        viewModelScope.launch(dispatchers.computation) {
            if (isFavorite) {
                addFavoriteUseCase(type, id)
            } else {
                delFavoriteUseCase(type, id)
            }
        }
        _favorite.value = isFavorite
        _event.value = EpisodeEvent.UPDATE_FAVORITE(id, isFavorite)
    }
}

@Suppress("SpellCheckingInspection")
private fun List<EpisodeInfo>.applyReaded(readList: List<Episode>) {
    for (readItem in readList) {
        for (episode in this) {
            if (readItem.episodeId == episode.id) {
                episode.isRead = true
                break
            }
        }
    }
}

@Suppress("ClassName")
sealed class EpisodeEvent {
    object START : EpisodeEvent()
    object LOADED : EpisodeEvent()
    class FIRST(val firstEpisode: EpisodeInfo) : EpisodeEvent()
    object ERROR : EpisodeEvent()
    class UPDATE_FAVORITE(val id: String, val isFavorite: Boolean) : EpisodeEvent()
}
