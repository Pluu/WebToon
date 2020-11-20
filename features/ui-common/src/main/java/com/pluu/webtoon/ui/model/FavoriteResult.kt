package com.pluu.webtoon.ui.model

import java.io.Serializable

/**
 * 즐겨찾기 여부 업데이트 Pending Extra Data
 */
// TODO: 추후 Parcelable로 대응
class FavoriteResult(
    val id: String,
    val isFavorite: Boolean
) : Serializable
