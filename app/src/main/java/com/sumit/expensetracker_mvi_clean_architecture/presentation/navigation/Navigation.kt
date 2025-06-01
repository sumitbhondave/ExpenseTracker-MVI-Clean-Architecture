package com.sumit.expensetracker_mvi_clean_architecture.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{movieId}") {
        fun createRoute(movieId: Int) = "detail_screen/$movieId"
        const val ARG_MOVIE_ID = "movieId" // Argument key
    }
}

val movieDetailArguments = listOf(
    navArgument(Screen.Detail.ARG_MOVIE_ID) {
        type = NavType.IntType
        nullable = false // Or true if you want to handle null IDs, but generally IDs are non-null
    }
)
