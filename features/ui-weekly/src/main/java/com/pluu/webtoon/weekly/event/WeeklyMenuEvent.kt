package com.pluu.webtoon.weekly.event

import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM

sealed class WeeklyMenuEvent {
    class OnMenuClicked(val item: UI_NAV_ITEM) : WeeklyMenuEvent()
    object OnSettingClicked : WeeklyMenuEvent()
}

sealed class WeeklyEvent {
    class ErrorEvent(val message: String) : WeeklyEvent()
    class UpdatedFavorite(val result: FavoriteResult) : WeeklyEvent()
}
