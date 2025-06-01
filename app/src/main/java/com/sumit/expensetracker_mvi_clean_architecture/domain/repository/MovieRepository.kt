package com.sumit.expensetracker_mvi_clean_architecture.domain.repository

import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDetailsDto
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieResponse

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): MovieResponse
    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto
}
