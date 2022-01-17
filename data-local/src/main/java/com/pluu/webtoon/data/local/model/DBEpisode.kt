package com.pluu.webtoon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["service", "toonId", "episodeId"])
data class DBEpisode(
    @ColumnInfo
    val service: String,
    @ColumnInfo
    val toonId: String,
    @ColumnInfo
    val episodeId: String
)
