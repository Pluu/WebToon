package com.pluu.webtoon.data.local.common

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

/**
 * Helper class for working with the SQLiteDatabase.
 */
object SqliteDatabaseTestHelper {
    fun insertToon(
        service: String,
        toonId: String,
        helper: SqliteTestDbOpenHelper
    ) {
        val db: SQLiteDatabase = helper.writableDatabase
        val values = ContentValues()
        values.put("service", service)
        values.put("toonId", toonId)
        db.insertWithOnConflict(
            "DBToon", null, values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }

    fun insertEpisode(
        service: String,
        toonId: String,
        episodeId: String,
        helper: SqliteTestDbOpenHelper
    ) {
        val db: SQLiteDatabase = helper.writableDatabase
        val values = ContentValues()
        values.put("service", service)
        values.put("toonId", toonId)
        values.put("episodeId", episodeId)
        db.insertWithOnConflict(
            "DBEpisode", null, values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }

    fun createTable(helper: SqliteTestDbOpenHelper) {
        val db: SQLiteDatabase = helper.writableDatabase
        db.execSQL("CREATE TABLE IF NOT EXISTS `DBToon` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `service` TEXT, `toonId` TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS `DBEpisode` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `service` TEXT, `toonId` TEXT, `episodeId` TEXT)")
        db.close()
    }

    fun clearDatabase(helper: SqliteTestDbOpenHelper) {
        val db: SQLiteDatabase = helper.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS `DBToon`")
        db.execSQL("DROP TABLE IF EXISTS `DBEpisode`")
        db.close()
    }
}