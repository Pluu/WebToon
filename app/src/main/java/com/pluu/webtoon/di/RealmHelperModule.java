package com.pluu.webtoon.di;

import com.pluu.webtoon.db.RealmHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Realm Helper Module
 * Created by PLUUSYSTEM-NEW on 2016-12-04.
 */
@Module
public class RealmHelperModule {

    @Provides
    @Singleton
    static RealmHelper provideRealmHelper() {
       return new RealmHelper();
    }

}
