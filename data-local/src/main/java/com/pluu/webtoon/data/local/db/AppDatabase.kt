package com.pluu.webtoon.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.db.migration.migration_1_2
import com.pluu.webtoon.data.local.model.DBEpisode
import com.pluu.webtoon.data.local.model.DBToon

@Database(
    entities = [DBToon::class, DBEpisode::class],
    version = 2
)
internal abstract class AppDatabase : RoomDatabase() {
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
