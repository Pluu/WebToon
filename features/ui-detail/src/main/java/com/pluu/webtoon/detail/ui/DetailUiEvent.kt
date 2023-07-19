package com.pluu.webtoon.detail.ui

internal sealed class DetailUiEvent {
    data object OnBackPressed : DetailUiEvent()
    data object OnSharedPressed : DetailUiEvent()
    data object OnPrevPressed : DetailUiEvent()
    data object OnNextPressed : DetailUiEvent()
}