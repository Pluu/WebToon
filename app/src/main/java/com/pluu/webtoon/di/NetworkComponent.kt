package com.pluu.webtoon.di

import com.pluu.support.impl.NetworkSupportApi
import dagger.Component
import javax.inject.Singleton

/**
 * NetworkComponent
 * Created by pluu on 2017-04-30.
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {

    fun inject(api: NetworkSupportApi)

}
