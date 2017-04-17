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

    var networkComponent: NetworkComponent? = null
        private set
    var realmHelperComponent: RealmHelperComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        initApplicationComponent()
        Realm.init(this)
    }

    private fun initApplicationComponent() {
        this.networkComponent = DaggerNetworkComponent.builder().build()

        this.realmHelperComponent = DaggerRealmHelperComponent.builder().build()
    }

}
