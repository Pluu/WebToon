package com.pluu.webtoon

import android.app.Application

import com.pluu.webtoon.di.DaggerNetworkComponent
import com.pluu.webtoon.di.DaggerRealmHelperComponent
import com.pluu.webtoon.di.NetworkComponent
import com.pluu.webtoon.di.RealmHelperComponent

import io.realm.Realm

/**
 * Application Controller
 * Created by nohhs on 2015-03-17.
 */
class AppController : Application() {

    val networkComponent: NetworkComponent by lazy {
        DaggerNetworkComponent.builder().build()
    }
    val realmHelperComponent: RealmHelperComponent by lazy {
        DaggerRealmHelperComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}
