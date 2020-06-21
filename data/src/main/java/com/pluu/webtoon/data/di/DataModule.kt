package com.pluu.webtoon.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
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
    ): IDBHelper = AppDatabase.getInstance(context).roomDao()

    @Provides
    fun provideDefaultSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}
