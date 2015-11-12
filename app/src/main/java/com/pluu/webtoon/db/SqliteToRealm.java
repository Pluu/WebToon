package com.pluu.webtoon.db;

import android.content.Context;

/**
 * Sqlite to Realm Migrate
 * Created by nohhs on 2015-11-12.
 */
public class SqliteToRealm {

	private final DbOpenHelper helper;

	public SqliteToRealm(Context context) {
		helper = DbOpenHelper.getInstance(context);
	}

	public void migrateToon() {

	}

	public void migrateEpisode() {

	}
}
