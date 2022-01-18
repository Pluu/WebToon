package com.pluu.webtoon.data.repository

import com.pluu.webtoon.model.NAV_ITEM

interface SessionRepository {
    fun getDefaultWebToon(): NAV_ITEM
}