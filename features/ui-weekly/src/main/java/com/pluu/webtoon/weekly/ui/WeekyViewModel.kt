package com.pluu.webtoon.weekly.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.utils.coroutines.mapOnSuspend
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeekyViewModel @ViewModelInject constructor(
    @Assisted handle: SavedStateHandle,
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

    private val cacheLister = mutableListOf<ToonInfoWithFavorite>()

    private val ceh = CoroutineExceptionHandler { _, t ->
        _event.value = WeeklyEvent.ERROR(t.localizedMessage ?: "Unknown Message")
    }

    init {
        viewModelScope.launch {
            cacheLister.addAll(getWeekLoad())
            _listEvent.value = cacheLister
        }
    }

    private suspend fun getWeekLoad(): List<ToonInfoWithFavorite> =
        withContext(dispatchers.computation + ceh) {
            val apiResult: Result<List<ToonInfo>> = weeklyUseCase(weekPos)
            if (apiResult is Result.Success) {
                apiResult.data
                    .mapOnSuspend {
                        val isFavorite = hasFavoriteUseCase(type, it.id)
                        ToonInfoWithFavorite(it, isFavorite)
                    }
                    .sortedWith(compareBy<ToonInfoWithFavorite> {
                        !it.isFavorite
                    }.thenBy {
                        it.info.title
                    })
                    .toList()
            } else {
                emptyList()
            }
        }

    fun updateFavorite(id: String, isFavorite: Boolean) {
        // TODO: 캐시 데이터없이 데이터 갱신 개선 필요
        val item = cacheLister.first {
            it.info.id == id
        }
        item.isFavorite = isFavorite
        _listEvent.value = cacheLister
    }
}

sealed class WeeklyEvent {
    class ERROR(val message: String) : WeeklyEvent()
}
