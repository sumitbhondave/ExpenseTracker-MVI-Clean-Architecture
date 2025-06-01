package com.sumit.expensetracker_mvi_clean_architecture.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDetailsDto
import com.sumit.expensetracker_mvi_clean_architecture.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetailsDto? = null,
    val error: String? = null
)

sealed class DetailIntent {
    data class LoadMovieDetails(val movieId: Int) : DetailIntent()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val savedStateHandle: SavedStateHandle // For accessing navigation arguments
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    init {
        // Assuming 'movieId' is passed as a navigation argument
        savedStateHandle.get<Int>("movieId")?.let { movieId ->
            if (movieId != -1) { // Check for a valid movieId, -1 or similar could be a default/error
                processIntent(DetailIntent.LoadMovieDetails(movieId))
            } else {
                 _state.update { it.copy(error = "Invalid Movie ID passed.") }
            }
        } ?: _state.update { it.copy(error = "Movie ID not found in arguments.") }
    }

    fun processIntent(intent: DetailIntent) {
        viewModelScope.launch {
            when (intent) {
                is DetailIntent.LoadMovieDetails -> loadMovieDetails(intent.movieId)
            }
        }
    }

    private suspend fun loadMovieDetails(movieId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        getMovieDetailsUseCase(movieId)
            .onSuccess { details ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        movieDetails = details,
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
