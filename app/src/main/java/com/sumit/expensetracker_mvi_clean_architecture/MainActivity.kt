package com.sumit.expensetracker_mvi_clean_architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sumit.expensetracker_mvi_clean_architecture.presentation.detail.DetailScreen
import com.sumit.expensetracker_mvi_clean_architecture.presentation.home.HomeScreen
import com.sumit.expensetracker_mvi_clean_architecture.presentation.navigation.Screen
import com.sumit.expensetracker_mvi_clean_architecture.presentation.navigation.movieDetailArguments
import com.sumit.expensetracker_mvi_clean_architecture.ui.theme.ExpenseTrackerMVICleanArchitectureTheme // Ensure your theme name is correct
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Assuming your theme is named ExpenseTrackerMVCleaningArchitectureTheme
            // If not, replace with your actual theme name (e.g., MoviesAppTheme if you create one)
            ExpenseTrackerMVICleanArchitectureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = movieDetailArguments
        ) { backStackEntry ->
            // val movieId = backStackEntry.arguments?.getInt(Screen.Detail.ARG_MOVIE_ID) ?: -1 // ViewModel handles this with SavedStateHandle
            DetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}