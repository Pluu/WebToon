package com.pluu.webtoon.data.db

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.model.REpisode
import com.pluu.webtoon.data.model.RToon
import io.realm.Realm

/**
 * Realm Helper
 * Created by pluu on 2017-04-30.
 */
class RealmHelper : IDBHelper {

    override fun isFavorite(item: NAV_ITEM, id: String): Boolean =
        Realm.getDefaultInstance().where(RToon::class.java)
            .equalTo("service", item.name)
            .equalTo("toonId", id)
            .count() > 0

    override fun getEpisode(item: NAV_ITEM, id: String): List<REpisode> =
        Realm.getDefaultInstance().where(REpisode::class.java)
            .equalTo("service", item.name)
            .equalTo("toonId", id)
            .isNotNull("episodeId")
            .findAll()

    override fun addFavorite(item: NAV_ITEM, id: String) =
        with(Realm.getDefaultInstance()) {
            beginTransaction()
            createObject(RToon::class.java).apply {
                service = item.name
                toonId = id
            }
            commitTransaction()
        }

    override fun removeFavorite(item: NAV_ITEM, id: String) =
        with(Realm.getDefaultInstance()) {
            beginTransaction()
            where(RToon::class.java)
                .equalTo("service", item.name)
                .equalTo("toonId", id)
                .findFirst()
                ?.run { deleteFromRealm() }
            commitTransaction()
        }

    override fun readEpisode(service: NAV_ITEM, id: String, episodeId: String) =
        with(Realm.getDefaultInstance()) {
            beginTransaction()
            createObject(REpisode::class.java).apply {
                this.service = service.name
                this.toonId = id
                this.episodeId = episodeId
            }
            commitTransaction()
        }
}
