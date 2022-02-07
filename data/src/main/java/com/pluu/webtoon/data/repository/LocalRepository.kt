package com.pluu.webtoon.data.repository

import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.model.ToonId
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getFavorites(serviceName: String): Flow<Set<ToonId>>

    fun getReadEpisode(serviceName: String, id: String): Flow<List<Episode>>

    suspend fun addFavorite(item: Toon)

    suspend fun removeFavorite(item: Toon)

    suspend fun readEpisode(item: Episode)

    fun getDefaultWebToon(): NAV_ITEM
}