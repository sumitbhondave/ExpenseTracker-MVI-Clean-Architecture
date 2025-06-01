package com.sumit.expensetracker_mvi_clean_architecture.domain.usecase

import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieResponse
import com.sumit.expensetracker_mvi_clean_architecture.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Result<MovieResponse> {
        return try {
            Result.success(movieRepository.getPopularMovies(page))
        } catch (e: Exception) {
            // It's good practice to catch specific exceptions (e.g., IOException, HttpException)
            // and map them to domain-specific errors if needed.
            // For now, we'll catch generic Exception.
            Result.failure(e)
        }
    }
}
