package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.ui.model.FavoriteResult
import javax.inject.Inject

class ToonViewModel @Inject constructor() : ViewModel() {
    private val _updateEvent: MutableLiveData<FavoriteResult> = MutableLiveData()
    val updateEvent: LiveData<FavoriteResult>
        get() = _updateEvent

    fun updateFavorite(favorite: FavoriteResult) {
        _updateEvent.value = favorite
    }
}
