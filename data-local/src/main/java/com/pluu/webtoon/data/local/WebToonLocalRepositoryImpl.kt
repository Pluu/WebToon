package com.pluu.webtoon.data.local

import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.pref.PrefConfig
import com.pluu.webtoon.data.local.utils.toDBModel
import com.pluu.webtoon.data.local.utils.toModel
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.utils.com.pluu.webtoon.data.repository.WebToonLocalRepository
import javax.inject.Inject

class WebToonLocalRepositoryImpl @Inject constructor(
    private val roomDao: RoomDao,
    private val prefConfig: PrefConfig
) : WebToonLocalRepository {
    override suspend fun isFavorite(
        serviceName: String,
        id: String
    ): Boolean = roomDao.isFavorite(serviceName, id)

    override suspend fun getEpisode(
        serviceName: String,
        id: String
    ): List<Episode> = roomDao.getEpisode(serviceName, id)
        .map {
            it.toModel()
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