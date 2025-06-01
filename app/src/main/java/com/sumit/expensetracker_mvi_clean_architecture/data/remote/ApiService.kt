package com.sumit.expensetracker_mvi_clean_architecture.data.remote

import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDetailsDto
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        // IMPORTANT: Replace with your actual TMDB API Key
        // For a real app, store this securely (e.g., local.properties -> BuildConfig)
        const val API_KEY = "YOUR_TMDB_API_KEY_PLACEHOLDER"
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDetailsDto
}
