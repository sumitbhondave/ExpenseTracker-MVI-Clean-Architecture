package com.sumit.expensetracker_mvi_clean_architecture.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDto
import com.sumit.expensetracker_mvi_clean_architecture.domain.usecase.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val movies: List<MovieDto> = emptyList(),
    val error: String? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true
)

sealed class HomeIntent {
    object LoadPopularMovies : HomeIntent()
    object LoadMoreMovies : HomeIntent() // For pagination
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        processIntent(HomeIntent.LoadPopularMovies)
    }

    fun processIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.LoadPopularMovies -> loadPopularMovies(isLoadMore = false)
                is HomeIntent.LoadMoreMovies -> loadPopularMovies(isLoadMore = true)
            }
        }
    }

    private suspend fun loadPopularMovies(isLoadMore: Boolean) {
        val currentPage = if (isLoadMore) _state.value.currentPage + 1 else 1
        if (currentPage == 1) { // Initial load or refresh
             _state.update { it.copy(isLoading = true, error = null) }
        } else { // Loading more
             _state.update { it.copy(isLoading = true) } // Keep existing data, show loading for more
        }


        getPopularMoviesUseCase(page = currentPage)
            .onSuccess { movieResponse ->
                _state.update { currentState ->
                    val newMovies = if (isLoadMore) {
                        currentState.movies + movieResponse.results
                    } else {
                        movieResponse.results
                    }
                    currentState.copy(
                        isLoading = false,
                        movies = newMovies,
                        currentPage = movieResponse.page,
                        canLoadMore = movieResponse.page < movieResponse.totalPages,
                        error = null
                    )
                }
            }
            .onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "An unknown error occurred"
                    )
                }
            }
    }
}
