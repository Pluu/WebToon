package com.pluu.webtoon.db;

import android.content.Context;

import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;

import java.util.List;

/**
 * Sqlite to Realm Migrate
 * Created by nohhs on 2015-11-12.
 */
public class SqliteToRealm {

	public void migrateToon(Context context, RealmHelper realmHelper) {
		List<FavoriteItem> list = DbOpenHelper.getInstance(context).getFavoriteList();
		realmHelper.convertToon(list);
	}

	public void migrateEpisode(Context context, RealmHelper realmHelper) {
		List<EpisodeItem> list = DbOpenHelper.getInstance(context).getEpisodeListe();
		realmHelper.convertEpisode(list);
	}

	public void complete(Context context) {
		DbOpenHelper.getInstance(context).migrateComplete();
	}
}
