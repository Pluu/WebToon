package com.pluu.webtoon.di

import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.domain.usecase.AddFavoriteUseCase
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.domain.usecase.ReadUseCase
import com.pluu.webtoon.domain.usecase.RemoveFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UseCaseModule {
    @Provides
    fun provideAddFavoriteUseCase(
        dbHelper: IDBHelper
    ) = AddFavoriteUseCase(dbHelper)

    @Provides
    fun provideHasFavoriteUseCase(
        dbHelper: IDBHelper
    ) = HasFavoriteUseCase(dbHelper)

    @Provides
    fun provideReadEpisodeListUseCase(
        dbHelper: IDBHelper
    ) = ReadEpisodeListUseCase(dbHelper)

    @Provides
    fun provideReadUseCase(
        dbHelper: IDBHelper
    ) = ReadUseCase(dbHelper)

    @Provides
    fun provideRemoveFavoriteUseCase(
        dbHelper: IDBHelper
    ) = RemoveFavoriteUseCase(dbHelper)
}