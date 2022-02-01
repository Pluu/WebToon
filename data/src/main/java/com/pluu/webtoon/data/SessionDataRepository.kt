package com.pluu.webtoon.data

import com.pluu.webtoon.data.repository.LocalRepository
import com.pluu.webtoon.data.repository.SessionRepository
import com.pluu.webtoon.model.NAV_ITEM
import javax.inject.Inject

internal class SessionDataRepository @Inject constructor(
    private val localRepository: LocalRepository
) : SessionRepository {
    override fun getDefaultWebToon(): NAV_ITEM {
        return localRepository.getDefaultWebToon()
    }
}