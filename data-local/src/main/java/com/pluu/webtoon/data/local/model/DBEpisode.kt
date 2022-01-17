package com.pluu.webtoon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.ToonId

@Entity(primaryKeys = ["service", "toonId", "episodeId"])
data class DBEpisode(
    @ColumnInfo
    val service: String,
    @ColumnInfo
    val toonId: ToonId,
    @ColumnInfo
    val episodeId: EpisodeId
)
