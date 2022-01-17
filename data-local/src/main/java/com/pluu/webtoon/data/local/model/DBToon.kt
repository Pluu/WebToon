package com.pluu.webtoon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["service", "toonId"])
data class DBToon(
    @ColumnInfo
    val service: String,
    @ColumnInfo
    val toonId: String
)
