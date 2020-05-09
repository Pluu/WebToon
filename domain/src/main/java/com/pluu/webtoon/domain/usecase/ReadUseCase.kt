package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.domain.moel.DetailResult

class ReadUseCase(
    private val dbHelper: IDBHelper
) {
    suspend operator fun invoke(type: NAV_ITEM, item: DetailResult.Detail) {
        dbHelper.readEpisode(
            DBEpisode(
                service = type.name,
                toonId = item.webtoonId,
                episodeId = item.episodeId
            )
        )
    }
}
