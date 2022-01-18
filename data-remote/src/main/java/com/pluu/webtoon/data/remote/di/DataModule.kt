package com.pluu.webtoon.data.remote.di

import com.pluu.webtoon.data.remote.RemoteRepositoryImpl
import com.pluu.webtoon.data.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RemoteDataBindsModule {
    @Singleton
    @Binds
    abstract fun bindsRemoteRepository(
        repository: RemoteRepositoryImpl
    ): RemoteRepository
}