package com.sumit.expensetracker_mvi_clean_architecture.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sumit.expensetracker_mvi_clean_architecture.data.model.MovieDto

const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Popular Movies") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.movies.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null && state.movies.isEmpty()) {
                Text(
                    text = "Error: ${state.error}",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.movies, key = { it.id }) { movie ->
                        MovieItem(movie = movie, onMovieClick = { onMovieClick(movie.id) })
                    }

                    if (state.canLoadMore && !state.isLoading) {
                        item {
                            Button(
                                onClick = { viewModel.processIntent(HomeIntent.LoadMoreMovies) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Load More")
                            }
                        }
                    } else if (state.isLoading && state.movies.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }

            // Show error toast for subsequent load failures
            if (state.error != null && state.movies.isNotEmpty()) {
                // Simple Snackbar or Toast can be shown here for errors during pagination
                // For brevity, not adding a full Snackbar implementation here.
                // Consider a UiEffect from ViewModel for this.
            }
        }
    }

    // Trigger load more when reaching the end of the list
    val currentListState = listState.layoutInfo
    LaunchedEffect(currentListState) {
        val visibleItemsInfo = currentListState.visibleItemsInfo
        if (visibleItemsInfo.isNotEmpty() && state.canLoadMore && !state.isLoading) {
            val lastVisibleItemIndex = visibleItemsInfo.last().index
            if (lastVisibleItemIndex == state.movies.size - 1) { // If last item is visible
                // Debounce or ensure not already loading
                viewModel.processIntent(HomeIntent.LoadMoreMovies)
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: MovieDto,
    onMovieClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMovieClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(TMDB_IMAGE_BASE_URL + movie.posterPath)
                    .crossfade(true)
                    // Add placeholder/error drawables if desired
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                movie.releaseDate?.let {
                    Text(text = "Release: $it", style = MaterialTheme.typography.labelSmall)
                }
                movie.voteAverage?.let {
                    Text(text = "Rating: $it/10", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
