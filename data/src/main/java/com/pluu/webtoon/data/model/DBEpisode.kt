package com.pluu.webtoon.utils.com.pluu.webtoon.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBEpisode(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val service: String? = null,
    @ColumnInfo
    val toonId: String? = null,
    @NonNull
    @ColumnInfo
    val episodeId: String? = null
)
