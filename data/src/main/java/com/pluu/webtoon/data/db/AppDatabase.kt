package com.pluu.webtoon.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pluu.webtoon.data.dao.RoomDao
import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon
import com.pluu.webtoon.data.db.migration.migration_1_2

@Database(
    entities = [DBToon::class, DBEpisode::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Sample.db"
            ).addMigrations(migration_1_2)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
