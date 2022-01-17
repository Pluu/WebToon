package com.pluu.webtoon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.pluu.webtoon.model.ToonId

@Entity(primaryKeys = ["service", "toonId"])
data class DBToon(
    @ColumnInfo
    val service: String,
    @ColumnInfo
    val toonId: ToonId
)
