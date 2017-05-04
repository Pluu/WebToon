package com.pluu.webtoon.db

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.model.REpisode
import com.pluu.webtoon.model.RToon
import io.realm.Realm

/**
 * Realm Helper
 * Created by pluu on 2017-04-30.
 */
class RealmHelper {

    fun getFavoriteToon(item: NAV_ITEM, id: String) =
            Realm.getDefaultInstance().where(RToon::class.java)
                    .equalTo("service", item.name)
                    .equalTo("toonId", id)
                    .count() > 0

    fun getEpisode(item: NAV_ITEM, id: String): List<REpisode> =
            Realm.getDefaultInstance().where(REpisode::class.java)
                    .equalTo("service", item.name)
                    .equalTo("toonId", id)
                    .isNotNull("episodeId")
                    .findAll()

    fun addFavorite(item: NAV_ITEM, id: String) =
            with(Realm.getDefaultInstance()) {
                beginTransaction()
                createObject(RToon::class.java).apply {
                    service = item.name
                    toonId = id
                }
                commitTransaction()
            }

    fun removeFavorite(item: NAV_ITEM, id: String) =
            with(Realm.getDefaultInstance()) {
                beginTransaction()
                where(RToon::class.java)
                        .equalTo("service", item.name)
                        .equalTo("toonId", id)
                        .findFirst()
                        .run { deleteFromRealm() }
                commitTransaction()
            }

    fun readEpisode(service: NAV_ITEM, item: Detail) =
            with(Realm.getDefaultInstance()) {
                beginTransaction()
                createObject(REpisode::class.java).apply {
                    this.service = service.name
                    toonId = item.webtoonId
                    episodeId = item.episodeId
                }
                commitTransaction()
            }

}