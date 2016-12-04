package com.pluu.webtoon.di;

import com.pluu.support.impl.NetworkSupportApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * NetworkComponent
 * Created by PLUUSYSTEM-SURFACE on 2016-07-12.
 */
@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    void inject(NetworkSupportApi api);

}
