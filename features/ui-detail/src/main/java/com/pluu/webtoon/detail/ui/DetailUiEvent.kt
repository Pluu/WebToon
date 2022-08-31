package com.pluu.webtoon.detail.ui

internal sealed class DetailUiEvent {
    object OnBackPressed : DetailUiEvent()
    object OnSharedPressed : DetailUiEvent()
    object OnPrevPressed : DetailUiEvent()
    object OnNextPressed : DetailUiEvent()
}