package com.pluu.webtoon.data.local.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.pluu.webtoon.data.local.WebToonLocalRepositoryImpl
import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.db.AppDatabase
import com.pluu.webtoon.utils.com.pluu.webtoon.data.repository.WebToonLocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object LocalDataModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): RoomDao = AppDatabase.getInstance(context).roomDao()

    @Provides
    fun provideDefaultSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}

@InstallIn(SingletonComponent::class)
@Module
internal abstract class LocalDataBindsModule {
    @Binds
    abstract fun bindsLocalRepository(
        repository: WebToonLocalRepositoryImpl
    ): WebToonLocalRepository
}