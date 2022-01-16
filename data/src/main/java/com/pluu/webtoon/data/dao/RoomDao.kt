package com.pluu.webtoon.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon

/** DB Implementation of AndroidX Room */
@Dao
interface RoomDao {
    @Query("SELECT COUNT(*) FROM DBToon WHERE service = :serviceName AND toonId = :id")
    suspend fun isFavorite(serviceName: String, id: String): Boolean

    @Query("SELECT * FROM DBEpisode WHERE service = :serviceName AND toonId = :id")
    suspend fun getEpisode(serviceName: String, id: String): List<DBEpisode>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(item: DBToon)

    @Transaction
    @Delete
    suspend fun removeFavorite(item: DBToon)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun readEpisode(item: DBEpisode)
}
