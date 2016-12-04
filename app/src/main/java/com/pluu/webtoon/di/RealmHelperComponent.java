package com.pluu.webtoon.di;

import com.pluu.webtoon.ui.EpisodeFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * RealmHelperComponent
 * Created by PLUUSYSTEM-SURFACE on 2016-07-12.
 */
@Singleton
@Component(modules = {RealmHelperModule.class})
public interface RealmHelperComponent {

    void inject(EpisodeFragment api);

}
