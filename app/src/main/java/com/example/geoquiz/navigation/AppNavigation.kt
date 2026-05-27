package com.example.geoquiz.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.geoquiz.ui.screens.*
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // ✅ SINGLE shared ViewModel for entire app
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }

        composable("quiz") {
            QuizScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }

        composable("result") {
            ResultScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }
    }
}