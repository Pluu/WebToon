package com.pluu.webtoon.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;


/**
 * DbOpenHelper
 * Created by nohhs on 2015-03-17.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 2;
	private static String DB_PATH = "";
	private static final String DB_NAME = "weebtoon.db";

	private SQLiteDatabase db;

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

	private final String[] FAVORITE_COLUMNS = {
		FavoriteItem.SERVICE,
		FavoriteItem.WEBTOON,
		FavoriteItem.FAVORITE
	};

	private final String[] EPISODE_COLUMNS = {
		EpisodeItem.SERVICE,
		EpisodeItem.WEBTOON,
		EpisodeItem.EPISODE
	};

	private static DbOpenHelper mInstance;

	public static DbOpenHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DbOpenHelper(context);
		}
		return mInstance;
	}

	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null /* factory */, VERSION);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
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

	private String getPath() {
		return DB_PATH + DB_NAME;
	}

	public boolean checkDatabase() {
		String path = getPath();
		if (TextUtils.isEmpty(path)) {
			return false;
		}

		File dbFile = new File(path);
		return dbFile.exists();
	}

	public boolean openDatabase() throws IOException {
		String path = getPath();
		//Log.v("mPath", mPath);
		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		//mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		return db != null;
	}

	public List<FavoriteItem> getFavoriteList()  {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(FavoriteItem.TABLE, FAVORITE_COLUMNS, null, null, null, null, null);

		List<FavoriteItem> list;
		if (cursor.moveToFirst()) {
			int size = cursor.getCount();
			list = new ArrayList<>(size);
			do {
				FavoriteItem item = new FavoriteItem();
				item.service = cursor.getString(cursor.getColumnIndex(FavoriteItem.SERVICE));
				item.webtoon = cursor.getString(cursor.getColumnIndex(FavoriteItem.WEBTOON));
				item.favorite = cursor.getInt(cursor.getColumnIndex(FavoriteItem.FAVORITE));
				list.add(item);
			} while (cursor.moveToNext());
		} else {
			list = Collections.emptyList();
		}

		cursor.close();

		return list;
	}

	public List<EpisodeItem> getEpisodeListe() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(EpisodeItem.TABLE, EPISODE_COLUMNS, null, null, null, null, null);

		List<EpisodeItem> list;
		if (cursor.moveToFirst()) {
			int size = cursor.getCount();
			list = new ArrayList<>(size);
			do {
				EpisodeItem item = new EpisodeItem();
				item.service = cursor.getString(cursor.getColumnIndex(EpisodeItem.SERVICE));
				item.webtoon = cursor.getString(cursor.getColumnIndex(EpisodeItem.WEBTOON));
				item.episode = cursor.getString(cursor.getColumnIndex(EpisodeItem.EPISODE));
				list.add(item);
			} while (cursor.moveToNext());
		} else {
			list = Collections.emptyList();
		}

		cursor.close();
		return list;
	}

	public void migrateComplete() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(FavoriteItem.TABLE, null, null);
		db.delete(EpisodeItem.TABLE, null, null);
	}

	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();
		}
		super.close();
	}
}
