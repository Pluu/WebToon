package com.pluu.webtoon.data.db

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.model.REpisode

interface IDBHelper {
    fun isFavorite(item: NAV_ITEM, id: String): Boolean

    fun getEpisode(item: NAV_ITEM, id: String): List<REpisode>

    fun addFavorite(item: NAV_ITEM, id: String)

    fun removeFavorite(item: NAV_ITEM, id: String)

    fun readEpisode(service: NAV_ITEM, id: String, episodeId: String)
}
