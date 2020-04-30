package com.pluu.webtoon.utils.com.pluu.webtoon.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.data.model.DBToon
import com.pluu.webtoon.utils.com.pluu.webtoon.data.model.DBEpisode

/** DB Implementation of AndroidX Room */
@Dao
interface RoomDao : IDBHelper {
    @Query("SELECT COUNT(*) FROM DBToon WHERE service = :serviceName AND toonId = :id")
    override suspend fun isFavorite(serviceName: String, id: String): Boolean

    @Query("SELECT * FROM DBEpisode WHERE service = :serviceName AND toonId = :id")
    override suspend fun getEpisode(serviceName: String, id: String): List<DBEpisode>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun addFavorite(item: DBToon)

    @Transaction
    @Delete
    override suspend fun removeFavorite(item: DBToon)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun readEpisode(item: DBEpisode)

}
