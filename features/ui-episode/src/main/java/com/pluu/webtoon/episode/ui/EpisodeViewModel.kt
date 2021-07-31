package com.pluu.webtoon.episode.ui

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
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfoWithFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** EpisodeInfo ViewModel */
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    handle: SavedStateHandle,
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

    private val _listEvent = MutableLiveData<Result<List<EpisodeInfo>>>()
    val listEvent: LiveData<Result<List<EpisodeInfo>>> get() = _listEvent

    private val _event = MutableLiveData<EpisodeEvent>()
    val event: LiveData<EpisodeEvent> get() = _event

    private val _readIdSet = MutableLiveData<Set<EpisodeId>>()
    val readIdSet: LiveData<Set<EpisodeId>> get() = _readIdSet

    private val _favorite = MutableLiveData(item.isFavorite)
    val favorite: LiveData<Boolean> get() = _favorite

    @Suppress("PrivatePropertyName")
    private val INIT_PAGE = 0
    private var pageNo = INIT_PAGE
    private var firstEpisode: EpisodeInfo? = null

    fun initialize() {
        pageNo = INIT_PAGE
        isNext = true
        _listEvent.value = Result.Success(emptyList())
    }

    fun load() {
        if (!isNext || _event.value is EpisodeEvent.START) return
        isNext = !isNext

        viewModelScope.launch {
            _event.value = EpisodeEvent.START(pageNo > INIT_PAGE)

            when (val episodePage = getEpisodeUseCase(id, pageNo)) {
                is Result.Success -> {
                    val data = episodePage.data
                    actionSuccessUi(data)
                    if (data.episodes.isNotEmpty()) {
                        _listEvent.value = Result.Success(data.episodes)
                    }
                }
                is Result.Error -> {
                    _listEvent.value = Result.Error(episodePage.throwable)
                }
            }

            _readIdSet.value = refreshReadId()
            _event.value = EpisodeEvent.LOADED

            if (isNext) {
                pageNo += 1
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
    }

    fun readUpdate() {
        viewModelScope.launch(dispatchers.main) {
            _readIdSet.value = refreshReadId()
            _event.value = EpisodeEvent.READED
        }
    }

    private suspend fun refreshReadId() = withContext(dispatchers.computation) {
        runCatching {
            readEpisodeListUseCase(type, id).asSequence()
                .mapNotNull { it.episodeId }
                .distinct()
                .toSet()
        }.getOrDefault(emptySet())
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
            withContext(dispatchers.main) {
                _favorite.value = isFavorite
                _event.value = EpisodeEvent.UPDATE_FAVORITE(id, isFavorite)
            }
        }
    }
}

@Suppress("ClassName")
sealed class EpisodeEvent {
    class START(val isOverFirstPage: Boolean) : EpisodeEvent()
    object LOADED : EpisodeEvent()
    object READED : EpisodeEvent()
    class FIRST(val firstEpisode: EpisodeInfo) : EpisodeEvent()
    class UPDATE_FAVORITE(val id: String, val isFavorite: Boolean) : EpisodeEvent()
}
