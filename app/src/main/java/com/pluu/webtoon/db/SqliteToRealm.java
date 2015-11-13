package com.pluu.webtoon.db;

import android.content.Context;

import java.util.List;

import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;

/**
 * Sqlite to Realm Migrate
 * Created by nohhs on 2015-11-12.
 */
public class SqliteToRealm {

	public void migrateToon(Context context) {
		List<FavoriteItem> list = DbOpenHelper.getInstance(context).getFavoriteList();
		RealmHelper.getInstance().convertToon(context, list);
	}

	public void migrateEpisode(Context context) {
		List<EpisodeItem> list = DbOpenHelper.getInstance(context).getEpisodeListe();
		RealmHelper.getInstance().convertEpisode(context, list);
	}

	public void complete(Context context) {
		DbOpenHelper.getInstance(context).migrateComplete();
	}
}
