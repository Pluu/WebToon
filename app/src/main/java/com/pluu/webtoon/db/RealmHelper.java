package com.pluu.webtoon.db;

import android.util.Log;

import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.db.item.EpisodeItem;
import com.pluu.webtoon.db.item.FavoriteItem;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.model.REpisode;
import com.pluu.webtoon.model.RToon;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Realm Helper
 * Created by PLUUSYSTEM-NEW on 2015-11-12.
 */
public class RealmHelper {

    private final String TAG = RealmHelper.class.getSimpleName();

    public void convertToon(List<FavoriteItem> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmList<RToon> toons = new RealmList<>();
        for (FavoriteItem item : list) {
            toons.add(new RToon(item));
        }
        Log.d(TAG, "Migrate Toon Count:" + list.size());

        realm.copyToRealm(toons);
        realm.commitTransaction();
    }

    public void convertEpisode(List<EpisodeItem> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmList<REpisode> episodes = new RealmList<>();
        for (EpisodeItem item : list) {
            episodes.add(new REpisode(item));
        }
        Log.d(TAG, "Migrate Episode Count:" + list.size());

        realm.copyToRealm(episodes);
        realm.commitTransaction();
    }

    public boolean getFavoriteToon(NAV_ITEM item, String id) {
        return Realm.getDefaultInstance().where(RToon.class)
                .equalTo("service", item.name())
                .equalTo("toonId", id)
                .count() > 0;
    }

    public List<REpisode> getEpisode(NAV_ITEM item, String id) {
        return Realm.getDefaultInstance().where(REpisode.class)
                .equalTo("service", item.name())
                .equalTo("toonId", id)
                .isNotNull("episodeId")
                .findAll();
    }

    public void addFavorite(NAV_ITEM item, String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RToon toon = realm.createObject(RToon.class);
        toon.setService(item.name());
        toon.setToonId(id);
        realm.commitTransaction();
    }

    public void removeFavorite(NAV_ITEM item, String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RToon toon = realm.where(RToon.class)
                .equalTo("service", item.name())
                .equalTo("toonId", id)
                .findFirst();
        toon.deleteFromRealm();
        realm.commitTransaction();
    }

    public void readEpisode(NAV_ITEM service, Detail item) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        REpisode episode = realm.createObject(REpisode.class);
        episode.setService(service.name());
        episode.setToonId(item.webtoonId);
        episode.setEpisodeId(item.episodeId);
        realm.commitTransaction();
    }

}
