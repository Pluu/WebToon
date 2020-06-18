package com.pluu.webtoon.ui.episode

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.common.Session
import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.domain.moel.Episode
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.AddFavoriteUseCase
import com.pluu.webtoon.domain.usecase.EpisodeUseCase
import com.pluu.webtoon.domain.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.domain.usecase.RemoveFavoriteUseCase
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * EpisodeInfo ViewModel
 */
class EpisodeViewModel @ViewModelInject constructor(
    @Assisted handle: SavedStateHandle,
    private val session: Session,
    private val dispatchers: AppCoroutineDispatchers,
    private val episodeUseCase: EpisodeUseCase,
    private val readEpisodeListUseCase: ReadEpisodeListUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val delFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    // TODO
    private val type = session.navi
    private val info = handle.get<ToonInfo>(Const.EXTRA_EPISODE)!!

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

    private val _favorite = MutableLiveData(info.isFavorite)
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

            when (val episodePage = getEpisodeUseCase(info.id, pageNo)) {
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
        readEpisodeListUseCase(type, info.id)
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
                addFavoriteUseCase(type, info.id)
            } else {
                delFavoriteUseCase(type, info.id)
            }
        }
        info.isFavorite = isFavorite

        _favorite.value = isFavorite
        _event.value = EpisodeEvent.UPDATE_FAVORITE(info.id, info.isFavorite)
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
