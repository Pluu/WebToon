package com.pluu.webtoon.weekly

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.utils.coroutines.mapOnSuspend
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
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

    private val _listEvent = MutableLiveData<List<ToonInfo>>()
    val listEvent: LiveData<List<ToonInfo>>
        get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent>
        get() = _event

    init {
        viewModelScope.launch {
            _event.value = WeeklyEvent.START
            _listEvent.value = getWeekLoad()
            _event.value = WeeklyEvent.LOADED
        }
    }

    private suspend fun getWeekLoad(): List<ToonInfo> = withContext(dispatchers.computation) {
        val apiResult: Result<List<ToonInfo>> = weeklyUseCase(weekPos)
        if (apiResult is Result.Success) {
            apiResult.data
                .mapOnSuspend {
                    it.isFavorite = hasFavoriteUseCase(type, it.id)
                    it
                }
                .sortedWith(compareBy<ToonInfo> {
                    !it.isFavorite
                }.thenBy {
                    it.title
                })
                .toList()
        } else {
            emptyList()
        }
    }
}

sealed class WeeklyEvent {
    object START : WeeklyEvent()
    object LOADED : WeeklyEvent()
    class ERROR(val message: String) : WeeklyEvent()
}
