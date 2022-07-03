package com.pluu.webtoon.data.di

import com.pluu.webtoon.data.SessionDataRepository
import com.pluu.webtoon.data.WebToonDataRepository
import com.pluu.webtoon.data.repository.SessionRepository
import com.pluu.webtoon.data.repository.WebToonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModuleBinds {
    @Binds
    abstract fun provideWebToonRepository(
        repository: WebToonDataRepository
    ): WebToonRepository

    @Singleton
    @Binds
    abstract fun bindsSessionRepository(
        repository: SessionDataRepository
    ): SessionRepository
}
