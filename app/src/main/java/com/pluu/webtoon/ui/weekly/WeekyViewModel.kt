package com.pluu.webtoon.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.usecase.HasFavoriteUseCase
import com.pluu.webtoon.usecase.WeeklyUseCase
import com.pluu.webtoon.utils.bgDispatchers
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.uiDispatchers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WeekyViewModel(
    private val weekPos: Int,
    private val weeklyUseCase: WeeklyUseCase,
    private val hasFavoriteUseCase: HasFavoriteUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val job by lazyNone { Job() }

    private val _listEvent = MutableLiveData<List<ToonInfo>>()
    val listEvent: LiveData<List<ToonInfo>>
        get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent>
        get() = _event

    init {
        GlobalScope.launch(uiDispatchers) {
            _event.value = WeeklyEvent.START

            val result = async(bgDispatchers) {
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
            }.await()

            _listEvent.value = result
            _event.value = WeeklyEvent.LOADED
        }
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}

sealed class WeeklyEvent {
    object START : WeeklyEvent()
    object LOADED : WeeklyEvent()
    class ERROR(val message: String) : WeeklyEvent()
}
