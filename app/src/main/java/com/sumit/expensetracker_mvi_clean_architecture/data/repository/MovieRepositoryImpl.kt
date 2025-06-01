package com.sumit.expensetracker_mvi_clean_architecture.data.repository

import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDetailsDto
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieResponse
import com.sumit.expensetracker_mvi_clean_architecture.data.remote.ApiService
import com.sumit.expensetracker_mvi_clean_architecture.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): MovieResponse {
        return apiService.getPopularMovies(page = page)
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return apiService.getMovieDetails(movieId = movieId)
    }
}
