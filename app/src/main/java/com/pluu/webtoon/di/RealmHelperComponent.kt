package com.pluu.webtoon.di

import com.pluu.webtoon.ui.*
import dagger.Component
import javax.inject.Singleton

/**
 * RealmHelperComponent
 * Created by pluu on 2017-04-30.
 */
@Singleton
@Component(modules = arrayOf(RealmHelperModule::class))
interface RealmHelperComponent {

    fun inject(activity: IntroActivity)
    fun inject(fragment: EpisodeFragment)
    fun inject(fragment: WebtoonListFragment)
    fun inject(activity: EpisodesActivity)
    fun inject(activity: DetailActivity)

}
