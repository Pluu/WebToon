package com.pluu.webtoon.data.db

import com.pluu.webtoon.data.model.DBToon
import com.pluu.webtoon.utils.com.pluu.webtoon.data.model.DBEpisode

interface IDBHelper {
    suspend fun isFavorite(serviceName: String, id: String): Boolean

    suspend fun getEpisode(serviceName: String, id: String): List<DBEpisode>

    suspend fun addFavorite(item: DBToon)

    suspend fun removeFavorite(item: DBToon)

    suspend fun readEpisode(item: DBEpisode)
}
