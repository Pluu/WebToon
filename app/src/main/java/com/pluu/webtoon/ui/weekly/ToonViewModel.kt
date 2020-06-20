package com.pluu.webtoon.ui.weekly

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.model.FavoriteResult

class ToonViewModel @ViewModelInject constructor() : ViewModel() {
    private val _updateEvent: MutableLiveData<FavoriteResult> = MutableLiveData()
    val updateEvent: LiveData<FavoriteResult>
        get() = _updateEvent

    fun updateFavorite(favorite: FavoriteResult) {
        _updateEvent.value = favorite
    }
}
