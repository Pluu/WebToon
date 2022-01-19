package com.pluu.webtoon.data.remote.di

import com.pluu.webtoon.data.remote.RemoteRepositoryImpl
import com.pluu.webtoon.data.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RemoteDataBindsModule {
    @Binds
    abstract fun bindsRemoteRepository(
        repository: RemoteRepositoryImpl
    ): RemoteRepository
}