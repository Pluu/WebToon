package com.pluu.webtoon.di

import android.content.Context
import com.pluu.webtoon.data.PrefConfig
import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): IDBHelper = AppDatabase.getInstance(context.applicationContext).roomDao()

    @Singleton
    @Provides
    fun providePrefConfig(
        @ApplicationContext context: Context
    ): PrefConfig = PrefConfig(context)
}
