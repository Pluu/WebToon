package com.pluu.webtoon.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pluu.webtoon.utils.com.pluu.webtoon.data.db.RoomDao
import com.pluu.webtoon.utils.com.pluu.webtoon.data.model.DBEpisode

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
