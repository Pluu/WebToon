package com.pluu.webtoon.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pluu.webtoon.data.local.model.DBEpisode
import com.pluu.webtoon.data.local.model.DBToon
import com.pluu.webtoon.model.ToonId
import kotlinx.coroutines.flow.Flow

/** DB Implementation of AndroidX Room */
@Dao
internal interface RoomDao {
    @Query("SELECT toonId FROM DBToon WHERE service = :serviceName")
    fun getFavorites(serviceName: String): Flow<List<ToonId>>

    @Query("SELECT * FROM DBEpisode WHERE service = :serviceName AND toonId = :id")
    fun getEpisode(serviceName: String, id: String): Flow<List<DBEpisode>>

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
