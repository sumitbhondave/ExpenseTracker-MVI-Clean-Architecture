package com.sumit.expensetracker_mvi_clean_architecture.di

import com.sumit.expensetracker_mvi_clean_architecture.data.repository.MovieRepositoryImpl
import com.sumit.expensetracker_mvi_clean_architecture.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}
