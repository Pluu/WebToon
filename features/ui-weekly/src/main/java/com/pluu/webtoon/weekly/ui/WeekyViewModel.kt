package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeekyViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val weeklyUseCase: WeeklyUseCase,
    private val hasFavoriteUseCase: HasFavoriteUseCase
) : ViewModel() {

    private val weekPos = handle.get<Int>("EXTRA_POS") ?: 0

    private val _listEvent = MutableLiveData<List<ToonInfoWithFavorite>>()
    val listEvent: LiveData<List<ToonInfoWithFavorite>> get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent> get() = _event

    // Cashing Data
    private val cacheList = mutableListOf<ToonInfo>()
    private val cacheFavorites = mutableSetOf<String>()

    private val ceh = CoroutineExceptionHandler { _, t ->
        _event.value = WeeklyEvent.ERROR(t.localizedMessage ?: "Unknown Message")
    }

    init {
        viewModelScope.launch {
            // Step1. 주간 웹툰 로드
            val tempList = getWeekLoad()
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

    private suspend fun getWeekLoad(): List<ToonInfo> =
        withContext(dispatchers.computation + ceh) {
            val apiResult: Result<List<ToonInfo>> = weeklyUseCase(weekPos)
            if (apiResult is Result.Success) {
                apiResult.data
            } else {
                emptyList()
            }
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

    private fun renderList(
        list: List<ToonInfo>,
        favoriteMap: Set<String>
    ) = viewModelScope.launch {
        _listEvent.value = list
            .map {
                ToonInfoWithFavorite(it, favoriteMap.contains(it.id))
            }
    }
}

sealed class WeeklyEvent {
    class ERROR(val message: String) : WeeklyEvent()
}
