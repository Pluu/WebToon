package com.pluu.webtoon.data.di

import com.pluu.webtoon.data.WebToonDataRepository
import com.pluu.webtoon.data.repository.LocalRepository
import com.pluu.webtoon.data.repository.RemoteRepository
import com.pluu.webtoon.data.repository.WebToonCacheRepository
import com.pluu.webtoon.data.repository.WebToonRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DataModule {
    @Singleton
    @Provides
    fun provideWebToonRepository(
        remoteRepository: RemoteRepository,
        localRepository: LocalRepository
    ): WebToonDataRepository = WebToonDataRepository(remoteRepository, localRepository)
}

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModuleBinds {
    @Binds
    abstract fun bindsCacheRepository(
        repository: WebToonDataRepository
    ): WebToonCacheRepository

    @Binds
    abstract fun bindsRepository(
        repository: WebToonDataRepository
    ): WebToonRepository
}
