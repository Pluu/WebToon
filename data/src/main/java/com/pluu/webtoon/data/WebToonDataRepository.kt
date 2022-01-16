package com.pluu.webtoon.utils.com.pluu.webtoon.data

import com.pluu.webtoon.data.dao.RoomDao
import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.utils.com.pluu.webtoon.data.utils.toDBModel
import com.pluu.webtoon.utils.com.pluu.webtoon.data.utils.toModel
import javax.inject.Inject

class WebToonDataRepository @Inject constructor(
    private val roomDao: RoomDao
) : WebToonRepository {
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
}