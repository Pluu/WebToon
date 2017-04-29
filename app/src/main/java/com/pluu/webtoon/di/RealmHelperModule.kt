package com.pluu.webtoon.di

import com.pluu.webtoon.db.RealmHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Realm Helper Module
 * Created by pluu on 2017-04-30.
 */
@Module
class RealmHelperModule {

    @Provides
    @Singleton
    fun provideRealmHelper(): RealmHelper = RealmHelper()

}
