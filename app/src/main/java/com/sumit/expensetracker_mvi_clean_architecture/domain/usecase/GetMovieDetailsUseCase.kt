package com.sumit.expensetracker_mvi_clean_architecture.domain.usecase

import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDetailsDto
import com.sumit.expensetracker_mvi_clean_architecture.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetailsDto> {
        return try {
            Result.success(movieRepository.getMovieDetails(movieId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
