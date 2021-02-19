package com.pluu.webtoon.detail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
internal data class DetailPageFieldValue(
    @Stable
    val isPrevEnabled: Boolean,
    @Stable
    val isNextEnabled: Boolean
)