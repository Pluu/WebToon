package com.pluu.webtoon.detail.ui

sealed class DetailUiEvent {
    object OnBackPressed : DetailUiEvent()
    object OnSharedPressed : DetailUiEvent()
    object OnPrevPressed : DetailUiEvent()
    object OnNextPressed : DetailUiEvent()
}