package com.pluu.webtoon.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.usecase.WeeklyUseCase
import com.pluu.webtoon.utils.bgDispatchers
import com.pluu.webtoon.utils.uiDispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class WeekyViewModel(
    private val serviceApi: AbstractWeekApi,
    private val weekPos: Int,
    private val useCase: WeeklyUseCase
) : ViewModel() {

    private val jobs = arrayListOf<Job>()

    private val _listEvent = MutableLiveData<List<WebToonInfo>>()
    val listEvent: LiveData<List<WebToonInfo>>
        get() = _listEvent

    private val _event = MutableLiveData<WeeklyEvent>()
    val event: LiveData<WeeklyEvent>
        get() = _event

    init {
        jobs += GlobalScope.launch(uiDispatchers) {
            _event.value = WeeklyEvent.START

            val result = async(bgDispatchers) {
                serviceApi.parseMain(weekPos)
                    .asSequence()
                    .map {
                        it.isFavorite = useCase.hasFavorite(it.toonId)
                        it
                    }
                    .sortedWith(compareBy<WebToonInfo> {
                        !it.isFavorite
                    }.thenBy {
                        it.title
                    })
                    .toList()
            }.await()

            _event.value = WeeklyEvent.LOADED
            _listEvent.value = result
        }
    }

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }
}

sealed class WeeklyEvent {
    object START : WeeklyEvent()
    object LOADED : WeeklyEvent()
    class ERROR(val message: String) : WeeklyEvent()
}
