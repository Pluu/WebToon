package com.pluu.webtoon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;


/**
 * DbOpenHelper
 * Created by nohhs on 2015-03-17.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 2;
	private static final String DB_NAME = "weebtoon.db";

	private static final String CREATE_ITEM = ""
		+ "CREATE TABLE " + EpisodeItem.TABLE + "("
		+ EpisodeItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
		+ EpisodeItem.SERVICE + " TEXT NOT NULL,"
		+ EpisodeItem.WEBTOON + " TEXT NOT NULL,"
		+ EpisodeItem.EPISODE + " TEXT NOT NULL"
		+ ")";

	private static final String CREATE_FAVORITE = ""
		+ "CREATE TABLE " + FavoriteItem.TABLE + "("
		+ FavoriteItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
		+ FavoriteItem.SERVICE + " TEXT NOT NULL,"
		+ FavoriteItem.WEBTOON + " TEXT NOT NULL,"
		+ FavoriteItem.FAVORITE + " INTEGER NOT NULL"
		+ ")";

	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null /* factory */, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ITEM);
		db.execSQL(CREATE_FAVORITE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1
			&& newVersion == 2) {
			db.execSQL(CREATE_FAVORITE);
		}

	}
}
