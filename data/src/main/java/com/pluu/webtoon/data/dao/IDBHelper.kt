package com.pluu.webtoon.data.dao

import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon

interface IDBHelper {
    suspend fun isFavorite(serviceName: String, id: String): Boolean

    suspend fun getEpisode(serviceName: String, id: String): List<DBEpisode>

    suspend fun addFavorite(item: DBToon)

    suspend fun removeFavorite(item: DBToon)

    suspend fun readEpisode(item: DBEpisode)
}
