package com.pluu.webtoon.data.local

import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.pref.PrefConfig
import com.pluu.webtoon.data.local.utils.toDBModel
import com.pluu.webtoon.data.local.utils.toModel
import com.pluu.webtoon.data.repository.LocalRepository
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.model.ToonId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalRepositoryImpl @Inject constructor(
    private val roomDao: RoomDao,
    private val prefConfig: PrefConfig
) : LocalRepository {
    override fun getFavorites(
        serviceName: String
    ): Flow<Set<ToonId>> = roomDao.getFavorites(serviceName)
        .map {
            it.toSet()
        }

    override fun getReadEpisode(
        serviceName: String,
        id: String
    ): Flow<List<Episode>> = roomDao.getEpisode(serviceName, id)
        .map {
            it.map { item -> item.toModel() }
        }

    override suspend fun addFavorite(item: Toon) {
        roomDao.addFavorite(item.toDBModel())
    }

    override suspend fun removeFavorite(item: Toon) {
        roomDao.removeFavorite(item.toDBModel())
    }

    override suspend fun readEpisode(item: Episode) {
        roomDao.readEpisode(item.toDBModel())
    }

    override fun getDefaultWebToon(): NAV_ITEM {
        return prefConfig.getDefaultWebToon()
    }
}