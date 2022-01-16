package com.pluu.webtoon.data.repository

import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.Toon

interface WebToonRepository {
    suspend fun isFavorite(serviceName: String, id: String): Boolean

    suspend fun getEpisode(serviceName: String, id: String): List<Episode>

    suspend fun addFavorite(item: Toon)

    suspend fun removeFavorite(item: Toon)

    suspend fun readEpisode(item: Episode)
}