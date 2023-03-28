package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.usecase.GetFavoritesUseCase
import com.pluu.webtoon.domain.usecase.site.GetWeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ToonId
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.model.WeekPosition
import com.pluu.webtoon.model.successOr
import com.pluu.webtoon.weekly.event.WeeklyEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class WeeklyDayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val getWeeklyUseCase: GetWeeklyUseCase,
    getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _listEvent = MutableLiveData<List<ToonInfoWithFavorite>>()
    val listEvent: LiveData<List<ToonInfoWithFavorite>> get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent> get() = _event

    private val favorites: Flow<Set<ToonId>> = getFavoritesUseCase(type)

    private val ceh = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
        _event.value = WeeklyEvent.ErrorEvent(t.localizedMessage ?: "Unknown Message")
    }

    private val weekPosition : Int = savedStateHandle.get<Int>(Const.EXTRA_WEEKLY_POSITION)!!

    private val toonList: Flow<List<ToonInfo>> = flow {
        emit(getWeekLoad(WeekPosition(weekPosition)))
    }

    init {
        viewModelScope.launch(ceh) {
            combine(
                toonList,
                favorites
            ) { toonList, favoriteSet ->
                toonList.map {
                    ToonInfoWithFavorite(it, favoriteSet.contains(it.id))
                }.sortedWith(compareBy<ToonInfoWithFavorite> {
                    it.isFavorite.not()
                }.thenBy {
                    it.info.title
                })
            }.collect {
                _listEvent.value = it
            }
        }
    }

    private suspend fun getWeekLoad(
        weekPosition: WeekPosition
    ): List<ToonInfo> = withContext(dispatchers.computation) {
        getWeeklyUseCase(weekPosition).successOr(emptyList())
    }

//    @AssistedFactory
//    interface Factory {
//        fun create(weekPosition: Int): WeeklyDayViewModel
//    }
}