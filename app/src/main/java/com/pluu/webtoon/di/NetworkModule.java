package com.pluu.webtoon.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp Network Provider
 * Created by PLUUSYSTEM-SURFACE on 2016-07-12.
 */
@Module
public class NetworkModule {

    @Provides @Singleton
    static OkHttpClient provideHeater() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

}
