package com.pluu.webtoon.utils.com.pluu.webtoon.data

import com.pluu.webtoon.data.repository.WebToonCacheRepository
import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.utils.com.pluu.webtoon.data.repository.WebToonLocalRepository
import javax.inject.Inject

class WebToonDataRepository @Inject constructor(
    private val localRepository: WebToonLocalRepository
) : WebToonRepository, WebToonCacheRepository {
    override suspend fun isFavorite(
        serviceName: String,
        id: String
    ): Boolean = localRepository.isFavorite(serviceName, id)

    override suspend fun getEpisode(
        serviceName: String,
        id: String
    ): List<Episode> = localRepository.getEpisode(serviceName, id)

    override suspend fun addFavorite(item: Toon) {
        localRepository.addFavorite(item)
    }

    override suspend fun removeFavorite(item: Toon) {
        localRepository.removeFavorite(item)
    }

    override suspend fun readEpisode(item: Episode) {
        localRepository.readEpisode(item)
    }

    override fun getDefaultWebToon(): NAV_ITEM {
        return localRepository.getDefaultWebToon()
    }
}