package com.pluu.webtoon.episode.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.usecase.AddFavoriteUseCase
import com.pluu.webtoon.domain.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.domain.usecase.RemoveFavoriteUseCase
import com.pluu.webtoon.domain.usecase.site.GetEpisodeUseCase
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ToonInfoWithFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** EpisodeInfo ViewModel */
@HiltViewModel
class EpisodeViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    readEpisodeListUseCase: ReadEpisodeListUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val delFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val item = handle.get<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)!!
    private val id = item.id

    private val _event = MutableLiveData<EpisodeEvent>()
    val event: LiveData<EpisodeEvent> get() = _event

    private val _favorite = MutableLiveData(item.isFavorite)
    val favorite: LiveData<Boolean> get() = _favorite

    private var firstEpisode: EpisodeInfo? = null

    val episodePage: Flow<PagingData<EpisodeInfo>> = Pager(
        PagingConfig(20)
    ) {
        EpisodeDataSource(id, getEpisodeUseCase)
    }.flow.cachedIn(viewModelScope)

    val readIdSet: StateFlow<Set<EpisodeId>> = readEpisodeListUseCase(type, id)
        .map { list ->
            list.asSequence()
                .mapNotNull { it.episodeId }
                .distinct()
                .toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptySet())

    fun requestFirst() = firstEpisode

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
    class FIRST(val firstEpisode: EpisodeInfo) : EpisodeEvent()
    class UPDATE_FAVORITE(val id: String, val isFavorite: Boolean) : EpisodeEvent()
}
