package com.pluu.webtoon.db;

import android.content.Context;

import java.util.List;

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

	private static RealmHelper mInstance;

	public static RealmHelper createInstance(){
		if (mInstance == null) {
			mInstance  = new RealmHelper();
		}
		return mInstance  = new RealmHelper();
	}
	public static RealmHelper getIntance(){
		return mInstance;
	}

	public void convertToon(Context context, List<FavoriteItem> list) {
		Realm realm = Realm.getInstance(context);
		realm.beginTransaction();

		RealmList<RToon> toons = new RealmList<>();
		for (FavoriteItem item : list) {
			toons.add(new RToon(item));
		}
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
		realm.copyToRealm(episodes);
		realm.commitTransaction();
	}

}
