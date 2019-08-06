package com.pluu.webtoon.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBToon(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val service: String?,
    @ColumnInfo
    val toonId: String?
)
