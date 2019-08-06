package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.utils.com.pluu.webtoon.data.model.DBEpisode

class ReadUseCase(
    private val dbHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    suspend operator fun invoke(item: DetailResult.Detail) {
        dbHelper.readEpisode(
            DBEpisode(
                id = 0,
                service = naviItem.name,
                toonId = item.webtoonId,
                episodeId = item.episodeId
            )
        )
    }
}
