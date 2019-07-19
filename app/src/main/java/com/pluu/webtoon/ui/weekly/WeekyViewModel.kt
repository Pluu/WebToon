package com.pluu.webtoon.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeekyViewModel(
    private val dispatchers: AppCoroutineDispatchers,
    private val weekPos: Int,
    private val weeklyUseCase: WeeklyUseCase,
    private val hasFavoriteUseCase: HasFavoriteUseCase
) : ViewModel() {

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
        val apiResult = weeklyUseCase(weekPos)
        if (apiResult is Result.Success) {
            runCatching {
                apiResult.data.asSequence()
                    .map {
                        it.isFavorite = hasFavoriteUseCase(it.id)
                        it
                    }
                    .sortedWith(compareBy<ToonInfo> {
                        !it.isFavorite
                    }.thenBy {
                        it.title
                    })
                    .toList()
            }.getOrDefault(emptyList())
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
