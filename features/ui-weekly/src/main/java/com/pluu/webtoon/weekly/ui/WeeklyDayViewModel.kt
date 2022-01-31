package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.site.GetWeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.model.WeekPosition
import com.pluu.webtoon.model.successOr
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.weekly.event.WeeklyEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class WeeklyDayViewModel @AssistedInject constructor(
    @Assisted val weekPosition: Int,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val getWeeklyUseCase: GetWeeklyUseCase,
    private val hasFavoriteUseCase: HasFavoriteUseCase
) : ViewModel() {

    private val _listEvent = MutableLiveData<List<ToonInfoWithFavorite>>()
    val listEvent: LiveData<List<ToonInfoWithFavorite>> get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent> get() = _event

    // Cashing Data
    private val cacheList = mutableListOf<ToonInfo>()
    private val cacheFavorites = mutableSetOf<String>()

    private val ceh = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
        _event.value = WeeklyEvent.ErrorEvent(t.localizedMessage ?: "Unknown Message")
    }

    init {
        viewModelScope.launch(ceh) {
            // Step1. 주간 웹툰 로드
            val tempList = getWeekLoad(WeekPosition(weekPosition))
            // Step2. 즐겨찾기 취득
            cacheFavorites.addAll(getFavorites(tempList))
            // Step3. 즐겨찾기 - 타이틀 순서로 정렬한 값을 리스트로 보관
            cacheList.addAll(
                tempList.sortedWith(compareBy<ToonInfo> {
                    cacheFavorites.contains(it.id).not()
                }.thenBy {
                    it.title
                })
            )
            // Step4. 화면 렌더링
            renderList(cacheList, cacheFavorites)
        }
    }

    private suspend fun getWeekLoad(
        weekPosition: WeekPosition
    ): List<ToonInfo> = withContext(dispatchers.computation) {
        getWeeklyUseCase(weekPosition).successOr(emptyList())
    }

    private suspend fun getFavorites(
        list: List<ToonInfo>
    ): Set<String> = withContext(dispatchers.computation + ceh) {
        list.filter {
            hasFavoriteUseCase(type, it.id)
        }.map {
            it.id
        }.toSet()
    }

    fun updateFavorite(id: String, isFavorite: Boolean) {
        if (isFavorite) {
            cacheFavorites.add(id)
        } else {
            cacheFavorites.remove(id)
        }
        renderList(cacheList, cacheFavorites)
    }

    fun updatedFavorite(favorite: FavoriteResult) {
        _event.value = WeeklyEvent.UpdatedFavorite(favorite)
    }

    private fun renderList(
        list: List<ToonInfo>,
        favoriteMap: Set<String>
    ) = viewModelScope.launch {
        _listEvent.value = list.map {
            ToonInfoWithFavorite(it, favoriteMap.contains(it.id))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(weekPosition: Int): WeeklyDayViewModel
    }
}