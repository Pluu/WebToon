package com.pluu.webtoon.di;

import com.pluu.webtoon.ui.DetailActivity;
import com.pluu.webtoon.ui.EpisodeFragment;
import com.pluu.webtoon.ui.EpisodesActivity;
import com.pluu.webtoon.ui.IntroActivity;
import com.pluu.webtoon.ui.WebtoonListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * RealmHelperComponent
 * Created by PLUUSYSTEM-SURFACE on 2016-07-12.
 */
@Singleton
@Component(modules = {RealmHelperModule.class})
public interface RealmHelperComponent {

    void inject(IntroActivity activity);
    void inject(EpisodeFragment fragment);
    void inject(WebtoonListFragment fragment);
    void inject(EpisodesActivity activity);
    void inject(DetailActivity activity);

}
