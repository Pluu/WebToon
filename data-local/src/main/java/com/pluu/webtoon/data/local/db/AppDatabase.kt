package com.pluu.webtoon.data.local.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.model.DBEpisode
import com.pluu.webtoon.data.local.model.DBToon

@Database(
    entities = [DBToon::class, DBEpisode::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = AppDatabase.AutoMigration_1_2::class
        )
    ]
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    @DeleteColumn(tableName = "DBToon", columnName = "id")
    @DeleteColumn(tableName = "DBEpisode", columnName = "id")
    class AutoMigration_1_2 : AutoMigrationSpec

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Sample.db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
