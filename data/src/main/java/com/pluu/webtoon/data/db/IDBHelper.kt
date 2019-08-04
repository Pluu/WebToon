package com.pluu.webtoon.data.db

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.model.REpisode

interface IDBHelper {
    suspend fun isFavorite(item: NAV_ITEM, id: String): Boolean

    suspend fun getEpisode(item: NAV_ITEM, id: String): List<REpisode>

    suspend fun addFavorite(item: NAV_ITEM, id: String)

    suspend fun removeFavorite(item: NAV_ITEM, id: String)

    suspend fun readEpisode(service: NAV_ITEM, id: String, episodeId: String)
}
