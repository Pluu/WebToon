package com.pluu.webtoon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.IOException;

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

	private static DbOpenHelper mInstance;

	public static DbOpenHelper createInstance(Context context){
		return mInstance = new DbOpenHelper(context);
	}
	public static DbOpenHelper getInstance(){
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

	public boolean openDatabase() throws IOException {
		String mPath = DB_PATH + DB_NAME;
		//Log.v("mPath", mPath);
		db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		//mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		return db != null;
	}

	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();
		}
		super.close();
	}
}
