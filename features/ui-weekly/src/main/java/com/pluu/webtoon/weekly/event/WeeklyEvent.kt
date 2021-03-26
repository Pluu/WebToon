package com.pluu.webtoon.weekly.event

import com.pluu.webtoon.weekly.model.UI_NAV_ITEM

sealed class WeeklyEvent {
    class OnMenuClicked(val item: UI_NAV_ITEM) : WeeklyEvent()
    object OnSettingClicked : WeeklyEvent()
}

class ErrorEvent(val message: String)