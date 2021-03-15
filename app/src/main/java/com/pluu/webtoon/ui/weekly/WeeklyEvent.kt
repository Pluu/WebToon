package com.pluu.webtoon.ui.weekly

import com.pluu.webtoon.model.UI_NAV_ITEM

sealed class WeeklyEvent {
    class OnMenuClicked(val item: UI_NAV_ITEM) : WeeklyEvent()
    object OnSettingClicked : WeeklyEvent()
}