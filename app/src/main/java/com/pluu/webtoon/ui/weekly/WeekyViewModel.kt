package com.pluu.webtoon.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.usecase.HasFavoriteUseCase
import com.pluu.webtoon.usecase.WeeklyUseCase
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import com.pluu.webtoon.utils.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
        dispatchers.main.launch {
            _event.value = WeeklyEvent.START

            val result = async(dispatchers.computation) {
                val apiResult = weeklyUseCase(weekPos)
                if (apiResult is Result.Success) {
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
                } else {
                    emptyList()
                }
            }

            _listEvent.value = result.await()
            _event.value = WeeklyEvent.LOADED
        }
    }
}

sealed class WeeklyEvent {
    object START : WeeklyEvent()
    object LOADED : WeeklyEvent()
    class ERROR(val message: String) : WeeklyEvent()
}
