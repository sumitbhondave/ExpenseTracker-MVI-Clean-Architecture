package com.sumit.expensetracker_mvi_clean_architecture.di

import com.sumit.expensetracker_mvi_clean_architecture.domain.repository.MovieRepository
import com.sumit.expensetracker_mvi_clean_architecture.domain.usecase.GetMovieDetailsUseCase
import com.sumit.expensetracker_mvi_clean_architecture.domain.usecase.GetPopularMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // Use cases are often scoped to ViewModels
object UseCaseModule {

    @Provides
    @ViewModelScoped // Scope to ViewModel lifecycle
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }

    @Provides
    @ViewModelScoped // Scope to ViewModel lifecycle
    fun provideGetMovieDetailsUseCase(movieRepository: MovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository)
    }
}
