package com.pluu.webtoon.data.local.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_1_2: Migration
    get() = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `DBToon_MERGE_TABLE` (`service` TEXT NOT NULL, `toonId` TEXT NOT NULL, PRIMARY KEY(`service`, `toonId`))")
            database.execSQL("INSERT INTO `DBToon_MERGE_TABLE` (`service`,`toonId`) SELECT `service`,`toonId` FROM `DBToon`")
            database.execSQL("DROP TABLE IF EXISTS `DBToon`")
            database.execSQL("ALTER TABLE `DBToon_MERGE_TABLE` RENAME TO `DBToon`")
            database.execSQL("CREATE TABLE IF NOT EXISTS `DBEpisode_MERGE_TABLE` (`service` TEXT NOT NULL, `toonId` TEXT NOT NULL, `episodeId` TEXT NOT NULL, PRIMARY KEY(`service`, `toonId`, `episodeId`))")
            database.execSQL("INSERT INTO `DBEpisode_MERGE_TABLE` (`service`,`toonId`,`episodeId`) SELECT `service`,`toonId`,`episodeId` FROM `DBEpisode`")
            database.execSQL("DROP TABLE IF EXISTS `DBEpisode`")
            database.execSQL("ALTER TABLE `DBEpisode_MERGE_TABLE` RENAME TO `DBEpisode`")
        }
    }
