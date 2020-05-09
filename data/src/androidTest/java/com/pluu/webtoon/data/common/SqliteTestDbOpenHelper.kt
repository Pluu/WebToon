package com.pluu.webtoon.data.common

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteTestDbOpenHelper(
    context: Context,
    databaseName: String
) : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE DBToon (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `service` TEXT, `toonId` TEXT)")
        db.execSQL("CREATE TABLE DBEpisode (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `service` TEXT, `toonId` TEXT, `episodeId` TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }

    companion object {
        const val DATABASE_VERSION = 1
    }
}