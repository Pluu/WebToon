package com.pluu.webtoon.db;

import android.content.Context;
import android.util.Log;

import java.util.List;

import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;
import com.pluu.webtoon.model.REpisode;
import com.pluu.webtoon.model.RToon;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Realm Helper
 * Created by PLUUSYSTEM-NEW on 2015-11-12.
 */
public class RealmHelper {

	private final String TAG = RealmHelper.class.getSimpleName();

	private static RealmHelper mInstance;

	public static RealmHelper getInstance() {
		if (mInstance == null) {
			mInstance = new RealmHelper();
		}
		return mInstance;
	}

	public void convertToon(Context context, List<FavoriteItem> list) {
		Realm realm = Realm.getInstance(context);
		realm.beginTransaction();

		RealmList<RToon> toons = new RealmList<>();
		for (FavoriteItem item : list) {
			toons.add(new RToon(item));
		}
		Log.d(TAG, "Migrate Toon Count:" + list.size());

		realm.copyToRealm(toons);
		realm.commitTransaction();
	}

	public void convertEpisode(Context context, List<EpisodeItem> list) {
		Realm realm = Realm.getInstance(context);
		realm.beginTransaction();

		RealmList<REpisode> episodes = new RealmList<>();
		for (EpisodeItem item : list) {
			episodes.add(new REpisode(item));
		}
		Log.d(TAG, "Migrate Episode Count:" + list.size());

		realm.copyToRealm(episodes);
		realm.commitTransaction();
	}

	public boolean getFavoriteToon(Context context, ServiceConst.NAV_ITEM item, String id) {
		return Realm.getInstance(context).where(RToon.class)
					.equalTo("service", item.name())
					.equalTo("toonId", id)
					.count() > 0;
	}

	public List<REpisode> getEpisode(Context context, ServiceConst.NAV_ITEM item, String id) {
		return Realm.getInstance(context).where(REpisode.class)
					.equalTo("service", item.name())
					.equalTo("toonId", id)
					.findAll();
	}

}
