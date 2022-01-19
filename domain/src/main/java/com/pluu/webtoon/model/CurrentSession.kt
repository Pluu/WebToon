package com.pluu.webtoon.model

import com.pluu.webtoon.data.repository.SessionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentSession @Inject constructor(
    repository: SessionRepository
) {
    var navi: NAV_ITEM = repository.getDefaultWebToon()
}