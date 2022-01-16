package com.pluu.webtoon.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.pluu.webtoon.data.dao.RoomDao
import com.pluu.webtoon.data.db.AppDatabase
import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.utils.com.pluu.webtoon.data.WebToonDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DataModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): RoomDao = AppDatabase.getInstance(context).roomDao()

    @Provides
    fun provideDefaultSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Singleton
    @Provides
    fun provideWebToonRepository(
        roomDao: RoomDao
    ): WebToonRepository = WebToonDataRepository(roomDao)
}
