package com.pluu.webtoon.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pluu.webtoon.data.dao.RoomDao
import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon

@Database(
    entities = [DBToon::class, DBEpisode::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Sample.db"
            ).build()
        }
    }
}
