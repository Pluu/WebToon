package com.pluu.webtoon.di

import com.pluu.webtoon.ui.DetailActivity
import com.pluu.webtoon.ui.EpisodeFragment
import com.pluu.webtoon.ui.EpisodesActivity
import com.pluu.webtoon.ui.WebtoonListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * RealmHelperComponent
 * Created by pluu on 2017-04-30.
 */
@Singleton
@Component(modules = arrayOf(RealmHelperModule::class))
interface RealmHelperComponent {

    fun inject(fragment: WebtoonListFragment)
    fun inject(activity: EpisodesActivity)
    fun inject(fragment: EpisodeFragment)
    fun inject(activity: DetailActivity)

}
