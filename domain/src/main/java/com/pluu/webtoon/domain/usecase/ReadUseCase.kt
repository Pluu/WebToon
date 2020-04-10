package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.utils.com.pluu.webtoon.data.model.DBEpisode

class ReadUseCase(
    private val dbHelper: IDBHelper
) {
    suspend operator fun invoke(type: NAV_ITEM, item: DetailResult.Detail) {
        dbHelper.readEpisode(
            DBEpisode(
                id = 0,
                service = type.name,
                toonId = item.webtoonId,
                episodeId = item.episodeId
            )
        )
    }
}
